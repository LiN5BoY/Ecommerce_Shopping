package com.example.ecommerce_shopping.data

sealed class Category (val category : String) {

//    1 -> tab.text = "Japanese Toys"
//    2 -> tab.text = "American Toys"
//    3 -> tab.text = "Chinese Toys"
//    4 -> tab.text = "English Toys"
    object Japanese_Toys : Category ("Japanese Toys")
    object American_Toys : Category ("American Toys")
    object Chinese_Toys : Category ("Chinese Toys")
    object English_Toys : Category ("English Toys")

}