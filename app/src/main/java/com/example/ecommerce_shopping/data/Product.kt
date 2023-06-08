package com.example.ecommerce_shopping.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// data class 中的 data 关键字表示这个类是一个数据类
// 即它只包含静态属性和方法，而没有实例属性。
// 在 Android 中，数据保存在 Parcel 中是非常重要的，因为 Parcel 可以方便地传递对象。
// 在 Kotlin 中，使用 @Parcelize 注解可以将对象序列化为 Parcel，这使得将对象从一个应用程序传递到另一个应用程序变得更加容易。
@Parcelize
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
) : Parcelable{
    constructor() : this("0","","",0f,images = emptyList())
}