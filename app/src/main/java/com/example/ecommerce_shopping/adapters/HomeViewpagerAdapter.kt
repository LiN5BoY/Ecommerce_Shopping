package com.example.ecommerce_shopping.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeViewpagerAdapter(
    private val fragments : List<Fragment>,
    fm: FragmentManager,
    lifecycle : Lifecycle
)   //FragmentStateAdapter实现了StatefulAdapter用于保存Fragment的状态。
    //可以看到FragmentStateAdapter同样有缓存Fragment队列
    : FragmentStateAdapter(fm,lifecycle){
    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}