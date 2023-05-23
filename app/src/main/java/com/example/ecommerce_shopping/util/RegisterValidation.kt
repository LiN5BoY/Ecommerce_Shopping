package com.example.ecommerce_shopping.util
//sealed 密封
//需要知道自己有哪些子类 （有限子类）
//
//需要统一接口
sealed class RegisterValidation(){
    object Success : RegisterValidation()
    data class Failed(val message : String) : RegisterValidation()
}

data class RegisterFieldsState(
    val email : RegisterValidation,
    val password : RegisterValidation
)
