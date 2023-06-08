package com.example.ecommerce_shopping.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_shopping.R
import com.example.ecommerce_shopping.adapters.BestProductsAdapter
import com.example.ecommerce_shopping.databinding.FragmentBaseCategoryBinding
import com.example.ecommerce_shopping.util.showBottomNavigationView

//父类 open
open class BaseCategoryFragment : Fragment(R.layout.fragment_base_category) {

    private lateinit var binding : FragmentBaseCategoryBinding
    // 使用 val 变量来声明属性或静态变量，并使用 var 变量来声明局部变量或函数参数。
    protected val offerAdapter : BestProductsAdapter by lazy { BestProductsAdapter() }
    protected val bestProductsAdapter : BestProductsAdapter by lazy { BestProductsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOfferRv()
        setupBestProductsRv()

        bestProductsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product",it) }
            // findNavController() 函数返回一个 NavController 对象，该对象可用于管理应用程序中的导航栈。
            // 使用该函数，您可以向导航控制器发送导航请求，例如从主屏幕切换到应用程序的特定页面或从应用程序的特定页面返回到主屏幕。
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }

        offerAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product",it) }
            // findNavController() 函数返回一个 NavController 对象，该对象可用于管理应用程序中的导航栈。
            // 使用该函数，您可以向导航控制器发送导航请求，例如从主屏幕切换到应用程序的特定页面或从应用程序的特定页面返回到主屏幕。
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }

        binding.rvOfferProducts.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if(!recyclerView.canScrollVertically(1) && dx != 0){
                    onOfferPagingRequest()
                }
            }
        })


        // NestedScrollView 是一个用于嵌套滚动视图的控件，它可以包含多个子视图。
        // NestedScrollView 的 onScrollChangeListener 是一个用于监听滚动事件的回调函数，可以在滚动过程中动态更新视图的大小和位置。
        binding.nestedScrollBaseCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{
            // v:参数 v 是一个 View 对象，它是滚动视图的子视图。当滚动事件发生时，这个 View 对象会作为参数传递给回调函数。
            // scrollView:参数 scrollView 是一个 RecyclerView 或其他滚动视图对象，它是滚动事件的接收者。当滚动事件发生时，这个对象会作为参数传递给回调函数。
            // scrollState:参数 scrollState 是一个整数，表示滚动状态。它通常用于表示滚动的方向和速度。当滚动事件发生时，这个整数会作为参数传递给回调函数。
                v,_,scrollY,_,_ ->//_ 省略变量名
            if(v.getChildAt(0).bottom <= v.height + scrollY){
                onBestProductsPagingRequest()
            }
        })


    }


    fun showOfferLoading(){
        binding.offerProductsProgressBar.visibility = View.VISIBLE
    }

    fun hideOfferLoading(){
        binding.offerProductsProgressBar.visibility = View.GONE
    }

    fun showBestProductsLoading(){
        binding.bestProductsProgressBar.visibility = View.VISIBLE
    }

    fun hideBestProductsLoading(){
        binding.bestProductsProgressBar.visibility = View.GONE
    }

    // open fun 是一种函数扩展关键字，它可以用于扩展现有函数的功能。
    // open fun 关键字可以用于类、接口、枚举等类型中，用于在该类中的函数之外定义扩展函数。
    open fun onOfferPagingRequest(){

    }
    open fun onBestProductsPagingRequest(){

    }

    private fun setupBestProductsRv() {
        // apply 方法接受一个函数或操作符参数，并返回 this 引用
        binding.rvBestProducts.apply {
            layoutManager = GridLayoutManager(requireContext(),2, GridLayoutManager.VERTICAL,false)

            // 在 RecyclerView 中，使用适配器可以让代码更易于维护和扩展，因为每个适配器都负责处理特定类型的数据。
            // 此外，使用适配器还可以提高性能，因为 RecyclerView 不必每次请求数据时都重新创建新的对象。
            adapter = bestProductsAdapter
        }
    }

    private fun setupOfferRv() {
        binding.rvOfferProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = offerAdapter
        }
    }


    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
    }

}