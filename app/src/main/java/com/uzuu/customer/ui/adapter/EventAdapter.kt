package com.uzuu.customer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uzuu.customer.databinding.ItemEndHomeBinding
import com.uzuu.customer.databinding.ItemStartHomeBinding
import com.uzuu.customer.domain.model.Event

class EventAdapter(
    private val onClick: (Event) -> Unit
) : ListAdapter<Event, RecyclerView.ViewHolder>(DIFF) {

    companion object {
        private const val TYPE_START = 0
        private const val TYPE_END = 1

        private val DIFF = object : DiffUtil.ItemCallback<Event>() {
            override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem == newItem
            }
        }
    }

    // 🧠 Quyết định layout ở đây
    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)

        return if (item.status == "PENDING") {
            TYPE_START
        } else {
            TYPE_END
        }
    }

    // 🧱 ViewHolder 1
    inner class StartVH(val binding: ItemStartHomeBinding) :
        RecyclerView.ViewHolder(binding.root)

    // 🧱 ViewHolder 2
    inner class EndVH(val binding: ItemEndHomeBinding) :
        RecyclerView.ViewHolder(binding.root)

    // 🏗 create viewholder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            TYPE_START -> {
                val binding = ItemStartHomeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                StartVH(binding)
            }

            TYPE_END -> {
                val binding = ItemEndHomeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                EndVH(binding)
            }

            else -> throw IllegalArgumentException("Invalid viewType")
        }
    }

    // 🔗 bind data
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when (holder) {

            is StartVH -> {
                holder.binding.txtNameEventStart.text = item.name
                holder.binding.txtAddressStart.text = item.location
                val minPrice = item.ticketTypes.minOfOrNull { it.price }
                holder.binding.txtPriceStart.text = if (minPrice != null) "${minPrice.toLong()}đ" else "Liên hệ"
                holder.binding.txtStatusStart.text = item.status
                holder.binding.root.setOnClickListener { onClick(item) }
            }

            is EndVH -> {
                holder.binding.txtNameEventEnd.text = item.name
                holder.binding.txtAddressEnd.text = item.location
                val minPrice = item.ticketTypes.minOfOrNull { it.price }
                holder.binding.txtPriceEnd.text = if (minPrice != null) "${minPrice.toLong()}đ" else "Liên hệ"
                holder.binding.txtStatusEnd.text = item.status
                holder.binding.root.setOnClickListener { onClick(item) }
            }
        }
    }
}