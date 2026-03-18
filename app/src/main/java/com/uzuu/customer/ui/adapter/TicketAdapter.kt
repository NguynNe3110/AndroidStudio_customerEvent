package com.uzuu.customer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uzuu.customer.databinding.ItemTicketInHomeBinding
import com.uzuu.customer.domain.model.Tickets
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
class TicketAdapter(
    val onClick: (Tickets) -> Unit
) : ListAdapter<Tickets, TicketAdapter.VH>(DIFF) {

    class VH(val binding: ItemTicketInHomeBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Tickets>() {

            override fun areItemsTheSame(oldItem: Tickets, newItem: Tickets): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Tickets, newItem: Tickets): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemTicketInHomeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)

        // Ví dụ binding
//        holder.binding.txtStartDay.text = format(item.startTime)
//        holder.binding.txtEndDay.text = format(item.endTime)

        holder.binding.root.setOnClickListener {
            onClick(item)
        }
    }

    private fun format(time: Long): String {
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return format.format(Date(time))
    }
}