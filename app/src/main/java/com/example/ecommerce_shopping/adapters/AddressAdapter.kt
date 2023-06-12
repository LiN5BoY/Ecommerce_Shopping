package com.example.ecommerce_shopping.adapters


import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.ecommerce_shopping.R
import com.example.ecommerce_shopping.data.Address
import com.example.ecommerce_shopping.databinding.AddressRvItemBinding

class AddressAdapter : Adapter<AddressAdapter.AddressViewHolder>(){


    inner class AddressViewHolder(val binding : AddressRvItemBinding) :
            ViewHolder(binding.root){
        fun bind(address: Address,isSelected : Boolean) {
            binding.apply {
                buttonAddress.text = address.addressTitle
                if(isSelected){
                    buttonAddress.background = ColorDrawable(itemView.context.resources.getColor(R.color.g_blue))
                }else{
                    buttonAddress.background = ColorDrawable(itemView.context.resources.getColor(R.color.g_white))
                }
            }
        }

    }

    private val diffUtil = object : DiffUtil.ItemCallback<Address>() {
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem.addressTitle == newItem.addressTitle && oldItem.fullName == newItem.fullName
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }
    }

    // AsyncListDiffer 是 Kotlin 中用于比较两个列表是否不同的类。
    // 它实现了 Differ 接口，用于比较两个列表之间的差异，并且提供了一些方法来获取列表之间的差异信息。
    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(
            AddressRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    var selectedAddress = -1
    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = differ.currentList[position]
        holder.bind(address,selectedAddress == position)

        holder.binding.buttonAddress.setOnClickListener {
            if (selectedAddress >= 0)
                notifyItemChanged(selectedAddress)
            selectedAddress = holder.adapterPosition
            notifyItemChanged(selectedAddress)
            // ?.invoke 用于检查参数是否为空。
            // 如果参数为空，则不会执行任何操作，而是返回 null。
            // 如果参数不为空，则执行操作并返回结果。
            onClick?.invoke(address)
        }
    }

    init {
        differ.addListListener { _, _ ->
            notifyItemChanged(selectedAddress)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick : ((Address) -> Unit) ?= null
}