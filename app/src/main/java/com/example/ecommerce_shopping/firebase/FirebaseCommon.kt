package com.example.ecommerce_shopping.firebase

import com.example.ecommerce_shopping.data.CartProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseCommon (
    private val firestore : FirebaseFirestore,
    private val auth : FirebaseAuth
){

    private val cartCollection = firestore.collection("user").document(auth.uid!!).collection("cart")

    fun addProductToCart(
        cartProduct : CartProduct,
        onResult : (CartProduct?,Exception?) -> Unit
    ){
        cartCollection.document().set(cartProduct)
            .addOnSuccessListener {
                onResult(cartProduct,null)
            }
            .addOnFailureListener {
                onResult(null,it)
            }
    }

    fun increaseQuantity(documentId : String,onResult: (String?, Exception?) -> Unit){
        // Android Firestore.runTransaction 是一个方法，用于在 Firestore 数据库中执行事务。
        // 事务是一个批量操作的组合，可以执行多个操作，以确保它们在一起执行，并且不会相互干扰。
        firestore.runTransaction { transition ->
            val documentRef = cartCollection.document(documentId)
            val document = transition.get(documentRef)
            val productObject = document.toObject(CartProduct :: class.java)
            productObject?.let {cartProduct ->
                val newQuantity = cartProduct.quantity + 1
                // copy object
                val newProductObject = cartProduct.copy(quantity = newQuantity)
                transition.set(documentRef,newProductObject)
            }
        }
        .addOnSuccessListener {
            onResult(documentId,null)
        }
        .addOnFailureListener {
            onResult(null,it)
        }
    }



    fun decreaseQuantity(documentId : String,onResult: (String?, Exception?) -> Unit){
        // Android Firestore.runTransaction 是一个方法，用于在 Firestore 数据库中执行事务。
        // 事务是一个批量操作的组合，可以执行多个操作，以确保它们在一起执行，并且不会相互干扰。
        firestore.runTransaction { transition ->
            val documentRef = cartCollection.document(documentId)
            val document = transition.get(documentRef)
            val productObject = document.toObject(CartProduct :: class.java)
            productObject?.let {cartProduct ->
                val newQuantity = cartProduct.quantity - 1
                // copy object
                val newProductObject = cartProduct.copy(quantity = newQuantity)
                transition.set(documentRef,newProductObject)
            }
        }
            .addOnSuccessListener {
                onResult(documentId,null)
            }
            .addOnFailureListener {
                onResult(null,it)
            }
    }


    // enum class 是一种用于定义枚举类型的类。
    // 枚举类型是一种用于表示具有离散值的常量类型的类型。
    enum class QuantityChanging{
        INCREASE,
        DECREASE
    }




}