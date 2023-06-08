package com.example.ecommerce_shopping.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_shopping.databinding.SizeRvItemBinding


class SizesAdapter : RecyclerView.Adapter<SizesAdapter.SizesViewHolder>(){


    private var selectedPosition = -1

    inner class SizesViewHolder(private val binding : SizeRvItemBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(size : String,position : Int){
            binding.tvSize.text = size
            if(position == selectedPosition){
                //Size is selected
                binding.apply {
                    imageShadow.visibility = View.VISIBLE
                }
            }else{
                //Size is not selected
                binding.apply {
                    imageShadow.visibility = View.INVISIBLE
                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizesViewHolder {
        return SizesViewHolder(
            SizeRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    // OnBindViewHolder 方法通常在 RecyclerView 的适配器中调用，用于在每次数据更新时更新 ViewHolder 的外观。
    override fun onBindViewHolder(holder: SizesViewHolder, position: Int) {
        val size = differ.currentList[position]
        holder.bind(size,position)
        // 在 ViewHolder 中，有一个 itemView 参数，它代表了数据元素的视图。
        // itemView 参数是 ViewHolder 的缓存视图，它代表了数据元素的最终渲染视图。
        // 在每次数据更新时，RecyclerView 会调用 OnBindViewHolder 方法来更新 ViewHolder 中的数据元素，然后使用 itemView 参数来更新视图。
        holder.itemView.setOnClickListener{
            if(selectedPosition >= 0 )
                notifyItemChanged(selectedPosition)
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
            // ?.invoke 是一个用于安全调用不确定类型的方法的关键字。它通常用于处理不确定类型的数据，例如从数据库中检索数据或从网络中检索数据。
            onItemClick?.invoke(size)
        }
    }

    var onItemClick : ((String) -> Unit) ?= null



}