package com.example.ecommerce_shopping.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.ecommerce_shopping.data.CartProduct
import com.example.ecommerce_shopping.firebase.FirebaseCommon
import com.example.ecommerce_shopping.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth : FirebaseAuth,
    // Android 中的 Firebase Common 是一个组件，用于在 Firebase 应用程序中集成常见的功能，例如用户登录、认证、数据存储和查询等。
    // 它提供了一组通用的 API，使开发人员可以更轻松地构建和维护 Firebase 应用程序。
    private val firebaseCommon : FirebaseCommon
) : ViewModel() {

    private val _addToCart = MutableStateFlow<Resource<CartProduct>>(Resource.Unspecified())
    val addToCart = _addToCart.asStateFlow()



    fun addUpdateProductInCart(cartProduct: CartProduct){

        viewModelScope.launch {
            _addToCart.emit(Resource.Loading())
        }

        firestore.collection("user").document(auth.uid!!)
            .collection("cart")
            .whereEqualTo("product.id",cartProduct.product.id).get()
            .addOnSuccessListener {
                it.documents.let {
                    if (it.isEmpty()){
                        //Add new Product
                        addNewProduct(cartProduct)
                    }else{
                        val product = it.first().toObject(CartProduct::class.java)
                        if (product == cartProduct){
                            //Increase the quantity
                            val documentId = it.first().id
                            increaseQuantity(documentId,cartProduct)
                        }else{
                            //Add new Product
                            addNewProduct(cartProduct)
                        }
                    }
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _addToCart.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    private fun addNewProduct(cartProduct: CartProduct){
        firebaseCommon.addProductToCart(cartProduct){
                addedProduct,e ->
            viewModelScope.launch {
                if (e == null)
                    _addToCart.emit(Resource.Success(addedProduct!!))
                else
                    _addToCart.emit(Resource.Error(e.message.toString()))
            }
        }
    }

    private fun increaseQuantity(documentId : String, cartProduct: CartProduct){
        firebaseCommon.increaseQuantity(documentId){
            _,e ->
            viewModelScope.launch {
                if (e == null)
                    _addToCart.emit(Resource.Success(cartProduct))
                else
                    _addToCart.emit(Resource.Error(e.message.toString()))
            }
        }
    }


}