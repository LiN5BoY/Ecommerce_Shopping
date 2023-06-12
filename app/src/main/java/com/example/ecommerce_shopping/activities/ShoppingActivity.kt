package com.example.ecommerce_shopping.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ecommerce_shopping.R
import com.example.ecommerce_shopping.databinding.ActivityShoppingBinding
import com.example.ecommerce_shopping.util.Resource
import com.example.ecommerce_shopping.viewmodel.CartViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {

    //惰性初始化是一种常见的模式，直到第一次访问该属性的时候，才根据需要创建对象的一部分
    val binding by lazy {
        ActivityShoppingBinding.inflate(layoutInflater)
    }

    val viewModel by viewModels<CartViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //NavController字面意思就是导航控制器，它负责操作Navigation框架下的Fragment的跳转与退出、动画、监听当前Fragment信息
        // 当然这些是基本操作。但是更重要的是知道它可以使用的范围
        // 一般情况下我们以为它只能在Fragment里调用，实际情况下它在Activity里也可以调用。灵活的使用它
        // 可以帮你实现所有形式的页面跳转。除此之外你甚至还能使用TabLayout配合Navigation进行主页的分页设计
        // 极端点你甚至还能在某个分页里再次添加TabLayout配合Navigation进行嵌套设计。
        val navController = findNavController(R.id.shoppingHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)

        lifecycleScope.launchWhenStarted {
            viewModel.cartProducts.collectLatest {
                when(it){
                    is Resource.Success -> {
                        // ① ?. 用于处理可选类型，如果可选类型为空，则返回默认值，否则返回值。
                        // 具体来说，?. 会返回可选类型中值为 true 的元素的值，如果可选类型为空，则返回默认值。
                        // ② ?: 用于处理可选类型，如果可选类型为空，则抛出异常，否则返回默认值。
                        // 具体来说，?: 会返回可选类型中值为 true 的元素的值，如果可选类型为空，则抛出 IOException 异常。
                        val count = it.data ?. size ?: 0
                        val buttomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
                        buttomNavigation.getOrCreateBadge(R.id.cartFragment).apply {
                            number = count
                            backgroundColor = resources.getColor(R.color.g_blue)
                        }
                    }
                    else -> Unit

                }
            }
        }

    }
}