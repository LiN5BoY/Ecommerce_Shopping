package com.example.ecommerce_shopping.data
// data class 中的 data 关键字表示这个类是一个数据类
// 即它只包含静态属性和方法，而没有实例属性。
data class Product (
    val id: String,
    val name: String,
    val category: String,
    val price: Float,
    val offerPercentage: Float? = null,
    val description: String? = null,
    val colors: List<Int>? = null,
    val sizes: List<String>? = null,
    val images: List<String>
){
    constructor() : this("0","","",0f,images = emptyList())
}