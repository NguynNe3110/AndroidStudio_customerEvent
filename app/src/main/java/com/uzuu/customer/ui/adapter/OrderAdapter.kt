package com.uzuu.customer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uzuu.customer.databinding.ItemOrderBinding
import com.uzuu.customer.domain.model.Order
import java.text.NumberFormat
import java.util.Locale

class OrderAdapter : ListAdapter<Order, OrderAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(old: Order, new: Order) = old.id == new.id
            override fun areContentsTheSame(old: Order, new: Order) = old == new
        }
        private val fmt = NumberFormat.getNumberInstance(Locale("vi", "VN"))
    }

    inner class VH(val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val order = getItem(position)
        with(holder.binding) {
            tvOrderId.text        = "Đơn #${order.id}"
            tvOrderDate.text      = order.orderDate.take(10)   // "YYYY-MM-DD"
            tvTotalAmount.text    = "${fmt.format(order.totalAmount.toLong())}đ"
            tvPaymentMethod.text  = order.paymentMethod

            val (payLabel, payColor) = when (order.paymentStatus) {
                "PAID"    -> "● Đã thanh toán" to root.context.getColor(com.uzuu.customer.R.color.event_completed)
                "FAILED"  -> "● Thất bại"      to root.context.getColor(com.uzuu.customer.R.color.event_ongoing)
                else      -> "● Chờ thanh toán" to root.context.getColor(com.uzuu.customer.R.color.event_on_sale)
            }
            tvPaymentStatus.text = payLabel
            tvPaymentStatus.setTextColor(payColor)

            tvOrderStatus.text = when (order.orderStatus) {
                "CONFIRMED"  -> "Đã xác nhận"
                "CANCELLED"  -> "Đã huỷ"
                else         -> "Đang xử lý"
            }
        }
    }
}