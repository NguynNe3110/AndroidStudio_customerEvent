package com.uzuu.customer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uzuu.customer.databinding.ItemMyTicketBinding
import com.uzuu.customer.domain.model.MyTicket
import java.text.NumberFormat
import java.util.Locale

class MyTicketAdapter(
    private val onClick: (MyTicket) -> Unit
) : ListAdapter<MyTicket, MyTicketAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<MyTicket>() {
            override fun areItemsTheSame(old: MyTicket, new: MyTicket) = old.id == new.id
            override fun areContentsTheSame(old: MyTicket, new: MyTicket) = old == new
        }
    }

    inner class VH(val binding: ItemMyTicketBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemMyTicketBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val ticket = getItem(position)
        with(holder.binding) {
            tvEventName.text      = ticket.eventName
            tvTicketTypeName.text = ticket.ticketTypeName
            tvTicketCode.text     = "Mã: ${ticket.ticketCode}"

            // Badge trạng thái
            val (label, colorRes) = when (ticket.status) {
                "VALID"   -> "● Hợp lệ"    to com.uzuu.customer.R.color.event_completed
                "USED"    -> "● Đã dùng"   to com.uzuu.customer.R.color.blue_text_secondary
                "EXPIRED" -> "● Hết hạn"   to com.uzuu.customer.R.color.event_ongoing
                else      -> "● ${ticket.status}" to com.uzuu.customer.R.color.blue_text_secondary
            }
            tvStatus.text = label
            tvStatus.setTextColor(root.context.getColor(colorRes))

            root.setOnClickListener { onClick(ticket) }
        }
    }
}