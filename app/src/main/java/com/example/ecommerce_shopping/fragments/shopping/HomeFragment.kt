package com.example.ecommerce_shopping.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ecommerce_shopping.R
import com.example.ecommerce_shopping.adapters.HomeViewpagerAdapter
import com.example.ecommerce_shopping.databinding.FragmentHomeBinding
import com.example.ecommerce_shopping.fragments.categories.AmeToyCategoryFragment
import com.example.ecommerce_shopping.fragments.categories.ChiToyCategoryFragment
import com.example.ecommerce_shopping.fragments.categories.EngToyCategoryFragment
import com.example.ecommerce_shopping.fragments.categories.JapToyCategoryFragment
import com.example.ecommerce_shopping.fragments.categories.MainCategroyFragment
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment(R.layout.fragment_home){
    private lateinit var binding : FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoriesFraments = arrayListOf<Fragment>(
            MainCategroyFragment(),
            JapToyCategoryFragment(),
            AmeToyCategoryFragment(),
            ChiToyCategoryFragment(),
            EngToyCategoryFragment(),
            )
        //getChildFragmentManager是Fragment中的方法，不管是app包还是v4包，都有的方法
        //获取的是当前这个Fragment中子一级的Fragment的管理器，比如Activity中有个Fragment，Fragment里面又有Fragment。
        val viewPager2Adapter = HomeViewpagerAdapter(categoriesFraments,childFragmentManager,lifecycle)
        binding.viewpagerHome.adapter = viewPager2Adapter
        TabLayoutMediator(binding.tabLayout,binding.viewpagerHome){ tab , position ->
            when (position) {
                0 -> tab.text = "Home"
                1 -> tab.text = "Japanese Toys"
                2 -> tab.text = "American Toys"
                3 -> tab.text = "Chinese Toys"
                4 -> tab.text = "English Toys"
            }
            //attach 调用生命周期
        }.attach()

    }
}