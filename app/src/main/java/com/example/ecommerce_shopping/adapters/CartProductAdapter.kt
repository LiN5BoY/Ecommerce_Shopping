package com.example.ecommerce_shopping.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce_shopping.data.CartProduct
import com.example.ecommerce_shopping.databinding.CartProductItemBinding
import com.example.ecommerce_shopping.helper.getProductPrice

class CartProductAdapter : RecyclerView.Adapter<CartProductAdapter.CartProductViewHolder>(){

    // Inner Class 是一种在外部类中定义的类。
    // 内部类可以继承外部类，并且可以访问外部类的属性和方法。
    // Android RecyclerView 是一个用于显示大量数据列表的控件
    // 它类似于 ListView，但具有更多功能，例如可伸缩的列表、自适应布局、列表项动画等。
    inner class CartProductViewHolder(val binding : CartProductItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(cartProduct : CartProduct){
            binding.apply {
                //Glide 通过提供 Glide类的实例来管理图像加载和渲染过程。
                Glide.with(itemView).load(cartProduct.product.images[0]).into(imageCartProduct)
                tvProductCartName.text = cartProduct.product.name
                tvCartProductQuantity.text = cartProduct.quantity.toString()

                val priceAfterPercentage = cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price)
                tvProductCartPrice.text = "$ ${String.format("%.2f",priceAfterPercentage)}"

                // Color.TRANSPARENT 是一个常量，表示透明颜色。
                imageCartProductColor.setImageDrawable(ColorDrawable(cartProduct.selectedColor?: Color.TRANSPARENT))
                tvCartProductSize.text = cartProduct.selectedSize ?: "".also {
                    imageCartProductSize.setImageDrawable(ColorDrawable(Color.TRANSPARENT))
                }
            }
        }

    }


    private val diffCallback = object : DiffUtil.ItemCallback<CartProduct>(){
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }

    }

    // AsyncListDiffer 是 Kotlin 中用于比较两个列表是否不同的类。
    // 它实现了 Differ 接口，用于比较两个列表之间的差异，并且提供了一些方法来获取列表之间的差异信息。
    val differ = AsyncListDiffer(this,diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
        return CartProductViewHolder(
            CartProductItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) {
        val cartProduct = differ.currentList[position]
        holder.bind(cartProduct)
        holder.itemView.setOnClickListener {
            onProductClick?.invoke(cartProduct)
        }
        holder.binding.imagePlus.setOnClickListener {
            onPlusClick?.invoke(cartProduct)
        }
        holder.binding.imageMinus.setOnClickListener {
            onMinusClick?.invoke(cartProduct)
        }
    }

    var onProductClick : ((CartProduct) -> Unit) ?= null

    var onPlusClick : ((CartProduct) -> Unit) ?= null

    var onMinusClick : ((CartProduct) -> Unit) ?= null

}