package com.example.ecommerce_shopping.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce_shopping.ToyStoryApplication
import com.example.ecommerce_shopping.data.User
import com.example.ecommerce_shopping.util.RegisterValidation
import com.example.ecommerce_shopping.util.Resource
import com.example.ecommerce_shopping.util.validateEmail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UserAccountViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    // Firebase StorageReference 是 Firebase 存储服务的一部分，它提供了一种用于访问 Firebase 存储桶中的文件的引用。
    private val storage : StorageReference,
    app : Application
) : AndroidViewModel(app) {
    private val _user = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val user = _user.asStateFlow()

    private val _updateInfo = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val updateInfo = _updateInfo.asStateFlow()


    init {
        getUser()
    }


    fun getUser(){
        viewModelScope.launch {
            _user.emit(Resource.Loading())
        }

        firestore.collection("user").document(auth.uid!!).get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                user?.let {
                    viewModelScope.launch {
                        _user.emit(Resource.Success(it))
                    }
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _user.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun updateUser(user: User, imageUri: Uri?){
        val areInputsValid = validateEmail(user.email) is RegisterValidation.Success
                && user.firstName.trim().isNotEmpty()
                && user.lastName.trim().isNotEmpty()

        if(!areInputsValid){
            viewModelScope.launch {
                _user.emit(Resource.Error("Check your inputs"))
            }
            return
        }

        viewModelScope.launch {
            _updateInfo.emit(Resource.Loading())
        }

        if (imageUri == null){
            saveUserInformation(user,true)
        }else{
            saveUserInformationWithNewImage(user,imageUri)
        }

    }

    private fun saveUserInformationWithNewImage(user: User, imageUri: Uri) {
        viewModelScope.launch {
            try {
                // MediaStore.Images.Media 是一个类，它提供了访问 Android 中的照片库的方法。
                // getBitmap 是一个方法，它接受两个参数：一个 contentResolver 和一个 imageUri。
                // getApplication<ToyStoryApplication> 是一个修饰符，它告诉编译器该代码使用的是 ToyStoryApplication 类型的应用程序对象。
                // contentResolver 是一个对象，它代表应用程序的 ContentResolver 对象，用于访问应用程序的数据库和资源。
                // imageUri 是一个字符串，它指定要访问的照片库中的图片的 Uri。
                // 因此，这段代码的作用是获取指向 Android 照片库中的图像的 Bitmap，该图像的 Uri 为 imageUri。该方法在 MediaStore.Images.Media 类中实现，
                // 它使用 contentResolver 对象从 ContentProvider 中检索图像，并返回一个 Bitmap 对象。
                val imageBitMap = MediaStore.Images.Media.getBitmap(getApplication<ToyStoryApplication>().contentResolver,imageUri)
                val byteArrayOutputStream = ByteArrayOutputStream()
                // Bitmap 是一种图像数据存储在内存中的对象，它可以使用多种压缩算法进行压缩，以便在存储或传输图像时减少数据量。
                // Bitmap 的 compress 方法用于对 Bitmap 对象进行压缩，以便将其保存到磁盘或通过网络传
                imageBitMap.compress(Bitmap.CompressFormat.JPEG,95,byteArrayOutputStream)
                val imageByteArray = byteArrayOutputStream.toByteArray()
                val imageDirectory = storage.child("profileImages/${auth.uid}/${UUID.randomUUID().toString()}")
                // 图像数据写入到图像文件夹
                val result = imageDirectory.putBytes(imageByteArray).await()
                // 使用 Result 对象下载图像数据并将其存储到 URL 对象中。
                val imageUrl = result.storage.downloadUrl.await().toString()
                saveUserInformation(user.copy(imagePath = imageUrl),false)
            }catch (e : Exception){
                viewModelScope.launch {
                    _updateInfo.emit(Resource.Error(e.message.toString()))
                }
            }
        }
    }

    private fun saveUserInformation(user: User, shouldRetrievedOldImage: Boolean) {
        // Firestore.runTransaction() 方法是一个用于在 Firestore 数据库中执行异步事务的方法。
        // 它允许开发人员在事务中执行一系列操作，并在事务完成时执行回调函数。
        firestore.runTransaction {transaction->
            val documentRef = firestore.collection("user").document(auth.uid!!)

            if (shouldRetrievedOldImage){
                val currentUser = transaction.get(documentRef).toObject(User::class.java)
                val newUser = user.copy(imagePath = currentUser?.imagePath ?: "")
                transaction.set(documentRef,newUser)
            }else{
                transaction.set(documentRef,user)
            }
        }.addOnSuccessListener {
            viewModelScope.launch {
                _updateInfo.emit(Resource.Success(user))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _updateInfo.emit(Resource.Error(it.message.toString()))
            }
        }
    }

}