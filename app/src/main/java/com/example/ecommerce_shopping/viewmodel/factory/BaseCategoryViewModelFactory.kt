package com.example.ecommerce_shopping.viewmodel.factory


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ecommerce_shopping.data.Category
import com.example.ecommerce_shopping.viewmodel.CategoryViewModel
import com.google.firebase.firestore.FirebaseFirestore

class BaseCategoryViewModelFactory(
    private val firestore : FirebaseFirestore,
    private val category: Category
    // 在 Kotlin 中，ViewModelProvider.Factory 是一个用于创建 ViewModel 的工厂类。
    // 它的作用是从一个 ViewModelKey 中获取一个 ViewModel，并且可以自定义创建 ViewModel 的方式。
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryViewModel(firestore,category) as T
    }

}