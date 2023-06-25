package com.example.ecommerce_shopping.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_shopping.adapters.BillingProductsAdapter
import com.example.ecommerce_shopping.data.order.OrderStatus
import com.example.ecommerce_shopping.data.order.getOrderStatus
import com.example.ecommerce_shopping.databinding.FragmentOrderDetailBinding
import com.example.ecommerce_shopping.util.VerticleItemDecoration

class OrderDetailFragment : Fragment() {

    private lateinit var binding: FragmentOrderDetailBinding

    private val billingProductsAdapter by lazy { BillingProductsAdapter() }

    // "navArgs"是一个函数式接口，它接受一个 Navigable arguments 对象作为参数，
    // 并且返回一个"OrderDetailFragmentArgs"对象。Navigable arguments 对象是用于在应用程序中的页面之间传递数据的对象。
    private val args by navArgs<OrderDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val order = args.order

        setupOrderRv()

        binding.apply {

            tvOrderId.text = "Order #${order.orderId}"

            // Android 中的"stepView"是一种用于显示多个子视图之间的过渡效果的视图。
            // 它可以被用于布局中，用于分隔不同的子视图，并提供一种平滑的过渡效果，以使用户感到视图之间的转换是无缝的。
            stepView.setSteps(
                mutableListOf(
                    OrderStatus.Ordered.status,
                    OrderStatus.Confirmed.status,
                    OrderStatus.Shipped.status,
                    OrderStatus.Delivered.status,
                )
            )

            val currentOrderState = when(getOrderStatus(order.orderStatus)){
                is OrderStatus.Ordered -> 0
                is OrderStatus.Confirmed -> 1
                is OrderStatus.Shipped -> 2
                is OrderStatus .Delivered -> 3
                else -> 0
            }

            stepView.go(currentOrderState,false)
            if (currentOrderState ==3 ) {
                stepView.done(true)
            }

            tvFullName.text = order.address.fullName
            tvAddress.text = "${order.address.street} ${order.address.city}"
            tvPhoneNumber.text = order.address.phone
            tvTotalPrice.text = "$ ${order.totalPrice}"


        }

        billingProductsAdapter.differ.submitList(order.products)

    }

    private fun setupOrderRv() {
        binding.rvProducts.apply {
            adapter = billingProductsAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            addItemDecoration(VerticleItemDecoration())
        }
    }

}