package com.example.ecommerce_shopping.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce_shopping.data.order.Order
import com.example.ecommerce_shopping.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
):ViewModel(){

    private val _order = MutableStateFlow<Resource<Order>>(Resource.Unspecified())
    val order = _order.asStateFlow()


    fun placeOrder(order : Order){
        viewModelScope.launch {
            _order.emit(Resource.Loading())
        }
        // Firestore.runBatch() 是一个用于执行批量操作的 API 方法。
        // 它允许您在 Firestore 数据库中批量执行操作，而不必逐一地执行每个操作。
        firestore.runBatch{batch ->
            //TODO: Add the order into user-orders collection
            //TODO: Add the order into orders collection
            //TODO: Delete the products from user-cart collection

            firestore.collection("user")
                .document(auth.uid!!)
                .collection("orders")
                .document()
                .set(order)

            firestore.collection("orders")
                .document()
                .set(order)

            firestore.collection("user").document(auth.uid!!).collection("cart").get()
                .addOnSuccessListener {
                    it.documents.forEach{
                        // Reference 是一个用于访问 Firestore 数据库中的文档、集合、字段等的类。
                        // 在 Firestore 中，每个文档都有一个唯一的 id 属性，可以用来唯一地标识文档。
                        // Reference 类提供了多种方法，用于获取指向特定文档的引用
                        it.reference.delete()
                    }
                }
        }.addOnSuccessListener {
            viewModelScope.launch {
                _order.emit(Resource.Success(order))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _order.emit(Resource.Error(it.message.toString()))
            }
        }
    }

}