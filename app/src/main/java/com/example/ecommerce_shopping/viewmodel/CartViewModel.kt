package com.example.ecommerce_shopping.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce_shopping.data.CartProduct
import com.example.ecommerce_shopping.firebase.FirebaseCommon
import com.example.ecommerce_shopping.helper.getProductPrice
import com.example.ecommerce_shopping.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth : FirebaseAuth,
    private val firebaseCommon : FirebaseCommon
) : ViewModel(){
    // Android MutableStateFlow 是 Android Studio 3.6 中引入的一种新工具，用于管理应用程序中的状态(独立管理)。
    private val _cartProducts = MutableStateFlow<Resource<List<CartProduct>>>(Resource.Unspecified())
    // asStateFlow() 函数可以将 Kotlin 对象转换为 StateFlow，这使得可以使用 StateFlow 库来处理 Kotlin 对象的状态和数据流。
    val cartProducts = _cartProducts.asStateFlow()

    // DocumentSnapshot 是 Firebase Cloud Firestore 中的一个对象，它表示一个文档的快照 (即文档在查询时的结果集)。
    private var cartProductDocuments = emptyList<DocumentSnapshot>()


    // 在 Kotlin StateFlow 中，map 属性是一个可选的参数，用于将输入流映射为输出流。
    // 它通常用于将输入流中的元素与输出流中的元素进行比较和处理。
    var productPrice = cartProducts.map {
        when(it){
            is Resource.Success -> {
                // !! 用于检查一个变量是否为 null，如果为 null，则返回默认值，否则返回该变量的值。
                calculatePrice(it.data!!)
            }else -> null
        }
    }

    private val _deleteDialog = MutableSharedFlow<CartProduct>()
    val deleteDialog = _deleteDialog.asSharedFlow()

    fun deleteCartProduct(cartProduct: CartProduct) {

        // 在 Kotlin StateFlow 中，data 属性是一个 Flowable 对象，它包含了当前 StateFlow 的输入数据和输出数据。
        // 您可以使用 stateFlow.data 方法获取当前 StateFlow 的 data 属性，并对其进行访问和处理。
        val index = cartProducts.value.data?.indexOf(cartProduct)
        if (index != null && index != -1) {
            val documentId = cartProductDocuments[index].id
            // // !! 用于检查一个变量是否为 null，如果为 null，则返回默认值，否则返回该变量的值。
            firestore.collection("user").document(auth.uid!!).collection("cart").document(documentId).delete()
        }
    }



    private fun calculatePrice(data: List<CartProduct>): Float {
        return data.sumByDouble {
            // 引用每个传入的cartProduct
            cartProduct ->
            (cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price) * cartProduct.quantity).toDouble()
        }.toFloat()
    }



    init {
        getCartProducts()
    }


    private fun getCartProducts(){
        viewModelScope.launch {
            _cartProducts.emit(Resource.Loading())
        }

        firestore.collection("user").document(auth.uid!!)
            .collection("cart").addSnapshotListener{
            // addSnapshotListener() 是一个用于添加状态 snapshot 监听器的函数。
            // 状态 snapshot 是 StateFlow 库中的一种数据结构，它用于存储状态数据和状态变化的历史记录。
            // 使用 addSnapshotListener() 函数可以监听状态 snapshot 的变化，并在状态 snapshot 发生变化时执行相应的回调函数。
            // 状态 snapshot 的变化通常来自于应用程序中的状态变化，例如用户的操作、网络请求的响应等
                value,error ->
                if (error != null || value == null) {
                    viewModelScope.launch {
                        _cartProducts.emit(Resource.Error(error?.message.toString()))
                    }
                }else{
                    cartProductDocuments = value.documents
                    val cartProduct = value.toObjects(CartProduct::class.java)
                    viewModelScope.launch {
                        _cartProducts.emit(Resource.Success(cartProduct))
                    }
                }
            }
    }


    fun changeQuantity(
        cartProduct: CartProduct,
        quantityChanging: FirebaseCommon.QuantityChanging
    ){



        // 在 Kotlin StateFlow 中，data 属性是一个 Flowable 对象，它包含了当前 StateFlow 的输入数据和输出数据。
        // 您可以使用 stateFlow.data 方法获取当前 StateFlow 的 data 属性，并对其进行访问和处理。
        val index = cartProducts.value.data?.indexOf(cartProduct)

        /**
         * index could be equal to -1 if the function [getCartProducts]
         * delays which will also delay the result we expect to be inside the [_cartProducts]
         * and to prevent the app from the crashing,we make a check
         */

        if (index != null && index != -1) {
            val documentId = cartProductDocuments[index].id
            when(quantityChanging){
                FirebaseCommon.QuantityChanging.INCREASE -> {
                    viewModelScope.launch { _cartProducts.emit(Resource.Loading()) }
                    increaseQuantity(documentId)
                }
                FirebaseCommon.QuantityChanging.DECREASE -> {

                    if(cartProduct.quantity == 1){

                        viewModelScope.launch {
                            // emit 方法需要一个事件流作为参数，而 viewModelScope.launch 会创建一个事件流，并将其作为参数传递给 emit。
                            _deleteDialog.emit(cartProduct)
                        }
                        return
                    }

                    viewModelScope.launch { _cartProducts.emit(Resource.Loading()) }
                    decreaseQuantity(documentId)
                }

            }
        }
    }

    private fun decreaseQuantity(documentId: String) {
        firebaseCommon.decreaseQuantity(documentId){
            result,exception->
            if (exception != null)
                viewModelScope.launch {
                    _cartProducts.emit(Resource.Error(exception.message.toString()))
                }
        }
    }

    private fun increaseQuantity(documentId: String) {
        firebaseCommon.increaseQuantity(documentId){
                result,exception->
            if (exception != null)
                viewModelScope.launch {
                    _cartProducts.emit(Resource.Error(exception.message.toString()))
                }
        }
    }

}