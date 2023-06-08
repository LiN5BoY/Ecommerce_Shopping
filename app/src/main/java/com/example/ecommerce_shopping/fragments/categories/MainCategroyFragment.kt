package com.example.ecommerce_shopping.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce_shopping.R
import com.example.ecommerce_shopping.adapters.BestDealsAdapter
import com.example.ecommerce_shopping.adapters.BestProductsAdapter
import com.example.ecommerce_shopping.adapters.SpecialProductsAdapter
import com.example.ecommerce_shopping.databinding.FragmentMainCategoryBinding
import com.example.ecommerce_shopping.util.Resource
import com.example.ecommerce_shopping.util.showBottomNavigationView
import com.example.ecommerce_shopping.viewmodel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

private val TAG = "MainCategroyFragment"

// 而 EntryPoint 提供了一种在应用程序启动时动态添加功能的方式。
@AndroidEntryPoint
class MainCategroyFragment : Fragment(R.layout.fragment_main_category){
    private lateinit var binding : FragmentMainCategoryBinding
    private lateinit var  specialProductsAdapter: SpecialProductsAdapter
    private lateinit var bestDealsAdapter: BestDealsAdapter
    private lateinit var bestProductsAdapter: BestProductsAdapter


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
        setupBestDealsRv()
        setupBestProductsRv()


        specialProductsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product",it) }
            // findNavController() 函数返回一个 NavController 对象，该对象可用于管理应用程序中的导航栈。
            // 使用该函数，您可以向导航控制器发送导航请求，例如从主屏幕切换到应用程序的特定页面或从应用程序的特定页面返回到主屏幕。
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }

        bestDealsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product",it) }
            // findNavController() 函数返回一个 NavController 对象，该对象可用于管理应用程序中的导航栈。
            // 使用该函数，您可以向导航控制器发送导航请求，例如从主屏幕切换到应用程序的特定页面或从应用程序的特定页面返回到主屏幕。
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }

        bestProductsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product",it) }
            // findNavController() 函数返回一个 NavController 对象，该对象可用于管理应用程序中的导航栈。
            // 使用该函数，您可以向导航控制器发送导航请求，例如从主屏幕切换到应用程序的特定页面或从应用程序的特定页面返回到主屏幕。
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }


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

        lifecycleScope.launchWhenCreated {
            // collectLatest() 方法用于对序列中的元素进行聚合操作，并返回最新的结果
            viewModel.bestDealsProducts.collectLatest {
                when (it){
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        bestDealsAdapter.differ.submitList(it.data)
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

        lifecycleScope.launchWhenCreated {
            // collectLatest() 方法用于对序列中的元素进行聚合操作，并返回最新的结果
            viewModel.bestProducts.collectLatest {
                when (it){
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        bestProductsAdapter.differ.submitList(it.data)
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
        // NestedScrollView 是一个用于嵌套滚动视图的控件，它可以包含多个子视图。
        // NestedScrollView 的 onScrollChangeListener 是一个用于监听滚动事件的回调函数，可以在滚动过程中动态更新视图的大小和位置。
        binding.nestedScrollMainCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{
            // v:参数 v 是一个 View 对象，它是滚动视图的子视图。当滚动事件发生时，这个 View 对象会作为参数传递给回调函数。
            // scrollView:参数 scrollView 是一个 RecyclerView 或其他滚动视图对象，它是滚动事件的接收者。当滚动事件发生时，这个对象会作为参数传递给回调函数。
            // scrollState:参数 scrollState 是一个整数，表示滚动状态。它通常用于表示滚动的方向和速度。当滚动事件发生时，这个整数会作为参数传递给回调函数。
            v,_,scrollY,_,_ ->//_ 省略变量名
            if(v.getChildAt(0).bottom <= v.height + scrollY){
                viewModel.fetchBestProducts()
            }
        })
    }

    private fun setupBestProductsRv() {
        bestProductsAdapter = BestProductsAdapter()
        // apply 方法接受一个函数或操作符参数，并返回 this 引用
        binding.rvBestProducts.apply {
            layoutManager = GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)

            // 在 RecyclerView 中，使用适配器可以让代码更易于维护和扩展，因为每个适配器都负责处理特定类型的数据。
            // 此外，使用适配器还可以提高性能，因为 RecyclerView 不必每次请求数据时都重新创建新的对象。
            adapter = bestProductsAdapter
        }
    }

    private fun setupBestDealsRv() {
        bestDealsAdapter = BestDealsAdapter()
        // apply 方法接受一个函数或操作符参数，并返回 this 引用
        binding.rvBestDealsProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

            // 在 RecyclerView 中，使用适配器可以让代码更易于维护和扩展，因为每个适配器都负责处理特定类型的数据。
            // 此外，使用适配器还可以提高性能，因为 RecyclerView 不必每次请求数据时都重新创建新的对象。
            adapter = bestDealsAdapter
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

    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
    }

}