package com.uzuu.customer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uzuu.customer.databinding.ItemTicketInHomeBinding
import com.uzuu.customer.domain.model.CategoryTicket
import java.text.NumberFormat
import java.util.Locale

class CategoryTicketAdapter : ListAdapter<CategoryTicket, CategoryTicketAdapter.VH>(DIFF) {

    // Map lưu số lượng vé người dùng đã chọn: ticketTypeId → quantity
    private val quantities = mutableMapOf<Long, Int>()

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<CategoryTicket>() {
            override fun areItemsTheSame(old: CategoryTicket, new: CategoryTicket) =
                old.id == new.id
            override fun areContentsTheSame(old: CategoryTicket, new: CategoryTicket) =
                old == new
        }
        private val priceFormatter = NumberFormat.getNumberInstance(Locale("vi", "VN"))
    }

    inner class VH(val binding: ItemTicketInHomeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemTicketInHomeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val ticket = getItem(position)

        with(holder.binding) {
            tvTicketName.text = ticket.name
            tvTicketPrice.text = "${priceFormatter.format(ticket.price.toLong())}đ"
            tvQuantity.text = quantities.getOrDefault(ticket.id, 0).toString()

            btnPlus.setOnClickListener {
                val qty = quantities.getOrDefault(ticket.id, 0)
                if (qty < ticket.remainingQuantity) {
                    quantities[ticket.id] = qty + 1
                    tvQuantity.text = quantities[ticket.id].toString()
                }
            }

            btnMinus.setOnClickListener {
                val qty = quantities.getOrDefault(ticket.id, 0)
                if (qty > 0) {
                    quantities[ticket.id] = qty - 1
                    tvQuantity.text = quantities[ticket.id].toString()
                }
            }
        }
    }

    /** Trả về các vé đã chọn: Map<ticketTypeId, quantity> */
    fun getSelectedQuantities(): Map<Long, Int> =
        quantities.filter { it.value > 0 }

    /** Reset tất cả số lượng */
    fun resetQuantities() {
        quantities.clear()
        notifyDataSetChanged()
    }
}