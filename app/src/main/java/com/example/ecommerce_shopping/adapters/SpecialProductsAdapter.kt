package com.example.ecommerce_shopping.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce_shopping.data.Product
import com.example.ecommerce_shopping.databinding.SpecialRvItemBinding

class SpecialProductsAdapter : RecyclerView.Adapter<SpecialProductsAdapter.SpecialProductsViewHolder>(){

    // Inner Class 是一种在外部类中定义的类。
    // 内部类可以继承外部类，并且可以访问外部类的属性和方法。
    // Android RecyclerView 是一个用于显示大量数据列表的控件
    // 它类似于 ListView，但具有更多功能，例如可伸缩的列表、自适应布局、列表项动画等。
    inner class SpecialProductsViewHolder(private val binding : SpecialRvItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(product : Product){
            binding.apply {
                //Glide 通过提供 Glide类的实例来管理图像加载和渲染过程。
                Glide.with(itemView).load(product.images[0]).into(imageSpecialRvitem)
                tvSpecialProductName.text = product.name
                tvSpecialProductPrice.text = "$ " + product.price.toString()
            }
        }

    }


    private val diffCallback = object : DiffUtil.ItemCallback<Product>(){
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialProductsViewHolder {
        return SpecialProductsViewHolder(
            SpecialRvItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SpecialProductsViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
    }

    var onClick : ((Product) -> Unit) ?= null

}