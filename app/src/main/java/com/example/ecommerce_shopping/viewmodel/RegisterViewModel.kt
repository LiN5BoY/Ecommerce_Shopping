package com.example.ecommerce_shopping.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ecommerce_shopping.data.User
import com.example.ecommerce_shopping.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

//dagger
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth : FirebaseAuth
): ViewModel(){
    //MutableStateFlow
    //MutableStateFlow类是一个可变的单向数据流
    //可以让我们在应用程序的不同部分之间共享数据，当状态发生变化时，所有订阅该状态的观察者将会被通知。
    private val _register = MutableStateFlow<Resource<FirebaseUser>>(Resource.Loading())
    //Flow 按顺序发出多个值的数据流
    val register : Flow<Resource<FirebaseUser>> = _register


    fun createAccountWithEmailAndPassword(user : User,password : String){
        firebaseAuth.createUserWithEmailAndPassword(user.email,password)
            .addOnSuccessListener {
                //it 表示当前正在处理的对象或变量
                //?. "被称为安全调用操作符（Safe Call Operator）
                //它的作用是在对一个可空对象的属性或方法进行访问时，
                //如果该对象为 null，则不会引发空指针异常，
                //而是直接返回 null。这样可以避免代码在处理 null 引用时出现运行时异常。
                it.user?.let {
                    //如果 user 不为 null
                    //则会执行 let 函数中的操作，否则 let 函数不会执行，避免了 null 引用异常。
                    _register.value = Resource.Success(it)
                }
                //成功情况
            }.addOnFailureListener{
                //失败情况
                _register.value = Resource.Error(it.message.toString())
            }
    }
}