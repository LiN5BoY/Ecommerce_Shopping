package com.example.ecommerce_shopping.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.ecommerce_shopping.data.Category
import com.example.ecommerce_shopping.data.Product
import com.example.ecommerce_shopping.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel constructor(
    private val firestore : FirebaseFirestore,
    private val category : Category
) : ViewModel() {

    // Android MutableStateFlow 是 Android Studio 3.6 中引入的一种新工具，用于管理应用程序中的状态。
    private val _offerProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    // 使用 asStateFlow() 函数，开发人员可以将任意对象转换为 StateFlow,从而更方便地管理状态。
    val offerProducts = _offerProducts.asStateFlow()

    // Android MutableStateFlow 是 Android Studio 3.6 中引入的一种新工具，用于管理应用程序中的状态。
    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    // 使用 asStateFlow() 函数，开发人员可以将任意对象转换为 StateFlow,从而更方便地管理状态。
    val bestProduct = _bestProducts.asStateFlow()


    init {
        fetchOfferProducts()
        fetchBestProductsProducts()
    }


    fun fetchOfferProducts(){
        viewModelScope.launch {
            _offerProducts.emit(Resource.Loading())
        }
        firestore.collection("Products").whereEqualTo("category",category.category)
            .whereNotEqualTo("offerPercentage",null).get()
            .addOnSuccessListener {
                val products = it.toObjects(Product::class.java)
                // viewModelScope.launch 是一个用于在视图模型中启动一个新活动的 API。
                viewModelScope.launch {
                    _offerProducts.emit(Resource.Success(products))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _offerProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun fetchBestProductsProducts(){
        viewModelScope.launch {
            _bestProducts.emit(Resource.Loading())
        }
        firestore.collection("Products").whereEqualTo("category",category.category)
            .whereEqualTo("offerPercentage",null).get()
            .addOnSuccessListener {
                val products = it.toObjects(Product::class.java)
                // viewModelScope.launch 是一个用于在视图模型中启动一个新活动的 API。
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Success(products))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }

}