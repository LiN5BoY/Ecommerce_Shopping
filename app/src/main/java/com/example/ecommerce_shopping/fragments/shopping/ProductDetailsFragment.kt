package com.example.ecommerce_shopping.fragments.shopping


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce_shopping.R
import com.example.ecommerce_shopping.activities.ShoppingActivity
import com.example.ecommerce_shopping.adapters.ColorsAdapter
import com.example.ecommerce_shopping.adapters.SizesAdapter
import com.example.ecommerce_shopping.adapters.ViewPager2Images
import com.example.ecommerce_shopping.databinding.FragmentProductDetailsBinding
import com.example.ecommerce_shopping.util.hideBottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView


class ProductDetailsFragment : Fragment() {
    // 在 Kotlin 中，navArgs 是一个用于在 Android 导航过程中传递参数的数据结构。
    // navArgs 主要用于在应用程序的导航操作 (例如从主屏幕进入应用程序的特定页面) 时传递数据。
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var binding: FragmentProductDetailsBinding
    private val viewPagerAdapter by lazy { ViewPager2Images() }
    private val sizesAdapter by lazy{ SizesAdapter() }
    private val colorsAdapter by lazy{ ColorsAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hideBottomNavigationView()
        binding = FragmentProductDetailsBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        setupSizesRv()
        setupColorsRv()
        setupViewpager()

        binding.imageClose.setOnClickListener {
            // 当应用程序使用 NavController 导航时，NavController 会负责管理应用程序的导航层次结构。
            // navigateUp() 方法可以用于返回上一层级或导航到应用程序的根视图。
            findNavController().navigateUp()
        }

        binding.apply {
            tvProductsName.text = product.name
            tvProductsPrice.text = "$ ${product.price}"
            tvProductDescription.text = product.description

            if(product.colors.isNullOrEmpty())
                tvProductColors.visibility = View.INVISIBLE
            if(product.sizes.isNullOrEmpty())
                tvProducSize.visibility = View.INVISIBLE
        }

        viewPagerAdapter.differ.submitList(product.images)
        product.colors?.let {
            colorsAdapter.differ.submitList(it)
        }
        product.sizes?.let {
            sizesAdapter.differ.submitList(it)
        }

    }

    private fun setupViewpager() {
        binding.apply {
            viewPagerProductImages.adapter = viewPagerAdapter
        }
    }

    private fun setupColorsRv() {
        binding.rvColors.apply {
            adapter = colorsAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
    }

    private fun setupSizesRv() {
        binding.rvSizes.apply {
            adapter = sizesAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
    }


}