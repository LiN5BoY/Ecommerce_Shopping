package com.example.ecommerce_shopping.util


import android.view.View
import androidx.fragment.app.Fragment
import com.example.ecommerce_shopping.R
import com.example.ecommerce_shopping.activities.ShoppingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.hideBottomNavigationView(){
    val buttomNavigationView =
        (activity as ShoppingActivity).findViewById<BottomNavigationView>(
        com.example.ecommerce_shopping.R.id.bottomNavigation
    )
    buttomNavigationView.visibility = android.view.View.GONE
}


fun Fragment.showBottomNavigationView(){
    val buttomNavigationView = (activity as ShoppingActivity).findViewById<BottomNavigationView>(
        com.example.ecommerce_shopping.R.id.bottomNavigation
    )
    buttomNavigationView.visibility = android.view.View.VISIBLE
}