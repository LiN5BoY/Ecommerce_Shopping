package com.example.ecommerce_shopping.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce_shopping.data.Product
import com.example.ecommerce_shopping.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


// @Inject constructor 注解指定了一个构造函数，该构造函数在注入依赖项时被自动调用。
// 这个注解可以用于声明一个构造函数，该构造函数接受依赖项作为参数，并将它们注入到实例中。
// Hilt 提供了一种更灵活的方法来创建动态应用程序，
@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel(){


    // MutableStateFlow 是 Android StateFlow 的一个子框架，它提供了一种在多个组件之间共享状态的机制。
    // 它允许组件将状态传递给其他组件，并在状态变化时自动更新。
    // 与 StateFlow 不同，MutableStateFlow 允许组件在创建时没有状态，而是在运行时动态添加和删除状态。
    private val _specialProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())


    // StateFlow 是一个用于在应用程序中管理状态的框架。
    // 它提供了一种将状态与组件之间进行通信的方式，使得组件可以共享状态，并能够更新状态以适应用户的操作。
    // 它允许组件在创建、销毁或切换时保留状态，并允许组件在状态变化时自动更新。
    val specialProducts : StateFlow<Resource<List<Product>>> = _specialProducts

    init {
        fetchSpecialProducts()
    }

    fun fetchSpecialProducts(){
        viewModelScope.launch {
            _specialProducts.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .whereEqualTo("category","Special Products").get().addOnSuccessListener {result ->
                // toObjects() 方法是用于将一个类的实例转换为对象数组的方法。
                // Product::class.java 表示要转换的类的类名，其中 Product 是一个类名。
                val specialProductsList = result.toObjects(Product::class.java)

                viewModelScope.launch {
                    _specialProducts.emit(Resource.Success(specialProductsList))
                }

        }.addOnFailureListener {

                viewModelScope.launch {
                    _specialProducts.emit(Resource.Error(it.message.toString()))
                }

        }
    }

}