package com.example.ecommerce_shopping.util

sealed class Resource<T>(
    //?= 变量值可能为null
    val data : T?=null,
    val message : String?=null
){
    class Success<T>(data : T) : Resource<T>(data)
    class Error<T>(message : String) : Resource<T>(message = message)
    class Loading<T> : Resource<T>()
}
