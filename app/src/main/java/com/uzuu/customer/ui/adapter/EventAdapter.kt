package com.uzuu.customer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
            override fun areItemsTheSame(oldItem: Event, newItem: Event) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Event, newItem: Event) = oldItem == newItem
        }

        /**
         * Server trả về URL dạng http://localhost:8080/...
         * Trên điện thoại thực thì localhost không trỏ được tới máy tính.
         * Hàm này thay thế "localhost" bằng IP thực của máy chủ.
         */
        fun fixImageUrl(url: String?): String? {
            if (url.isNullOrBlank()) return null
            return url.replace("http://localhost", "http://192.168.1.9")
                .replace("https://localhost", "http://192.168.1.9")
        }
    }

    inner class StartVH(val binding: ItemStartHomeBinding) : RecyclerView.ViewHolder(binding.root)
    inner class EndVH(val binding: ItemEndHomeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int =
        if (getItem(position).status == "PENDING") TYPE_START else TYPE_END

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            TYPE_START -> StartVH(
                ItemStartHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            TYPE_END -> EndVH(
                ItemEndHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> throw IllegalArgumentException("Invalid viewType")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        val minPrice = item.ticketTypes.minOfOrNull { it.price }
        val priceText = if (minPrice != null) "${minPrice.toLong()}đ" else "Liên hệ"
        val imageUrl = fixImageUrl(item.imageUrls.firstOrNull())

        when (holder) {
            is StartVH -> with(holder.binding) {
                txtNameEventStart.text = item.name
                txtAddressStart.text = item.location
                txtPriceStart.text = priceText
                txtStatusStart.text = "● ${item.status}"
                txtTicketQuantityStart.text = "Số vé còn: ${item.ticketTypes.sumOf { it.remainingQuantity }}"

                Glide.with(imgEventStart)
                    .load(imageUrl)
                    .centerCrop()
                    .into(imgEventStart)

                root.setOnClickListener { onClick(item) }
            }

            is EndVH -> with(holder.binding) {
                txtNameEventEnd.text = item.name
                txtAddressEnd.text = item.location
                txtPriceEnd.text = priceText
                txtStatusEnd.text = "● ${item.status}"

                Glide.with(imgEventEnd)
                    .load(imageUrl)
                    .centerCrop()
                    .into(imgEventEnd)

                root.setOnClickListener { onClick(item) }
            }
        }
    }
}