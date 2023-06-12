package com.example.ecommerce_shopping.data.order

import com.example.ecommerce_shopping.data.Address
import com.example.ecommerce_shopping.data.CartProduct

data class Order (
    val orderStatus : String,
    val totalPrice : Float,
    val products : List<CartProduct>,
    val address: Address
){

}