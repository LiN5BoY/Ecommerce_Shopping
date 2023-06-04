package com.example.ecommerce_shopping.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce_shopping.R
import com.example.ecommerce_shopping.adapters.BestProductsAdapter
import com.example.ecommerce_shopping.databinding.FragmentBaseCategoryBinding

//父类 open
open class BaseCategoryFragment : Fragment(R.layout.fragment_base_category) {

    private lateinit var binding : FragmentBaseCategoryBinding
    private lateinit var offerAdapter : BestProductsAdapter
    private lateinit var bestProductsAdapter : BestProductsAdapter

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
    }

    private fun setupBestProductsRv() {
        bestProductsAdapter = BestProductsAdapter()
        // apply 方法接受一个函数或操作符参数，并返回 this 引用
        binding.rvBestProducts.apply {
            layoutManager = GridLayoutManager(requireContext(),2, GridLayoutManager.VERTICAL,false)

            // 在 RecyclerView 中，使用适配器可以让代码更易于维护和扩展，因为每个适配器都负责处理特定类型的数据。
            // 此外，使用适配器还可以提高性能，因为 RecyclerView 不必每次请求数据时都重新创建新的对象。
            adapter = bestProductsAdapter
        }
    }

    private fun setupOfferRv() {
        offerAdapter = BestProductsAdapter()
        binding.rvBestProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = offerAdapter
        }
    }

}