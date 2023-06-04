package com.example.ecommerce_shopping.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.ecommerce_shopping.data.Product
import com.example.ecommerce_shopping.databinding.BestDealsRvItemBinding
import com.example.ecommerce_shopping.databinding.ProductRvItemBinding

class BestProductsAdapter : RecyclerView.Adapter<BestProductsAdapter.BestProductsViewHolder>(){


    // Inner Class 是一种在外部类中定义的类。
    // 内部类可以继承外部类，并且可以访问外部类的属性和方法。
    // ViewHolder 的作用是减少 RecyclerView 的渲染开销，因为它可以将相同的渲染模板缓存起来，避免重复渲染。
    inner class BestProductsViewHolder(private val binding : ProductRvItemBinding) : ViewHolder(binding.root){
        fun bind(product: Product){
            binding.apply {
                //Glide 通过提供 Glide类的实例来管理图像加载和渲染过程。
                Glide.with(itemView).load(product.images[0]).into(imgProduct)
                product.offerPercentage?.let {
                    val remainingPricePercentage = 1f - it
                    val priceAfterOffer = remainingPricePercentage * product.price
                    tvNewPrice.text = "$ ${String.format("%.2f",priceAfterOffer)}"
                    //加上虚线
                    tvPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }

                if(product.offerPercentage == null)
                    tvNewPrice.visibility = View.INVISIBLE

                tvPrice.text = "$ ${product.price}"
                tvName.text = product.name
            }
        }
    }



    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    // AsyncListDiffer 是 Kotlin 中用于比较两个列表是否不同的类。
    // 它实现了 Differ 接口，用于比较两个列表之间的差异，并且提供了一些方法来获取列表之间的差异信息。
    val differ = AsyncListDiffer(this,diffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestProductsViewHolder {
        return BestProductsViewHolder(
            ProductRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BestProductsViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
    }


}