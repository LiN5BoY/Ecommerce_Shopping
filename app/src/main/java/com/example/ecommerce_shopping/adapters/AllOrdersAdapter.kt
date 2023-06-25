package com.example.ecommerce_shopping.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.ecommerce_shopping.R
import com.example.ecommerce_shopping.data.order.Order
import com.example.ecommerce_shopping.data.order.OrderStatus
import com.example.ecommerce_shopping.data.order.getOrderStatus
import com.example.ecommerce_shopping.databinding.OrderItemBinding


class AllOrdersAdapter : RecyclerView.Adapter<AllOrdersAdapter.OrdersViewHolder>() {

    inner class OrdersViewHolder(private val binding : OrderItemBinding)
        : ViewHolder(binding.root){
        fun bind(order: Order){
            binding.apply {
                tvOrderId.text = order.orderId.toString()
                tvOrderDate.text = order.date
                val resources = itemView.resources

               val colorDrawable = when (getOrderStatus(order.orderStatus)){
                   is OrderStatus.Ordered -> {
                       ColorDrawable(resources.getColor(R.color.g_orange_yellow))
                   }
                   is OrderStatus.Confirmed -> {
                       ColorDrawable(resources.getColor(R.color.g_green))
                   }
                   is OrderStatus.Delivered -> {
                       ColorDrawable(resources.getColor(R.color.g_green))
                   }
                   is OrderStatus.Shipped -> {
                       ColorDrawable(resources.getColor(R.color.g_green))
                   }
                   is OrderStatus.Canceled -> {
                       ColorDrawable(resources.getColor(R.color.g_red))
                   }
                   is OrderStatus.Returned -> {
                       ColorDrawable(resources.getColor(R.color.g_red))
                   }
               }

                imageOrderState.setImageDrawable(colorDrawable)

            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Order>(){
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.products == newItem.products
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        return OrdersViewHolder(
            OrderItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    // OnBindViewHolder 方法通常在 RecyclerView 的适配器中调用，用于在每次数据更新时更新 ViewHolder 的外观。
    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val order = differ.currentList[position]
        holder.bind(order)

        holder.itemView.setOnClickListener {
            onClick?.invoke(order)
        }

    }

    var onClick: ((Order) -> Unit) ?= null
}