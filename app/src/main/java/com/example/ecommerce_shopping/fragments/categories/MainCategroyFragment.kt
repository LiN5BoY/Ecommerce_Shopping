package com.example.ecommerce_shopping.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce_shopping.R
import com.example.ecommerce_shopping.adapters.SpecialProductsAdapter
import com.example.ecommerce_shopping.databinding.FragmentMainCategoryBinding
import com.example.ecommerce_shopping.util.Resource
import com.example.ecommerce_shopping.viewmodel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

private val TAG = "MainCategroyFragment"

// 而 EntryPoint 提供了一种在应用程序启动时动态添加功能的方式。
@AndroidEntryPoint
class MainCategroyFragment : Fragment(R.layout.fragment_main_category){
    private lateinit var binding : FragmentMainCategoryBinding
    private lateinit var  specialProductsAdapter: SpecialProductsAdapter
    private val viewModel by viewModels<MainCategoryViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpecialProductsRv()
        lifecycleScope.launchWhenCreated {
            // collectLatest() 方法用于对序列中的元素进行聚合操作，并返回最新的结果
            viewModel.specialProducts.collectLatest {
                when (it){
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        specialProductsAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error -> {
                        hideLoading()
                        Log.e(TAG,it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun hideLoading() {
        binding.mainCategoryProgressBar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.mainCategoryProgressBar.visibility = View.VISIBLE
    }

    private fun setupSpecialProductsRv() {
        specialProductsAdapter = SpecialProductsAdapter()
        // apply 方法接受一个函数或操作符参数，并返回 this 引用
        binding.rvSpecialProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

            // 在 RecyclerView 中，使用适配器可以让代码更易于维护和扩展，因为每个适配器都负责处理特定类型的数据。
            // 此外，使用适配器还可以提高性能，因为 RecyclerView 不必每次请求数据时都重新创建新的对象。
            adapter = specialProductsAdapter
        }
    }
}