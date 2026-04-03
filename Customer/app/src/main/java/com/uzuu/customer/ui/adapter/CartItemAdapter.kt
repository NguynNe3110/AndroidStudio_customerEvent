package com.uzuu.customer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uzuu.customer.databinding.ItemCartItemBinding
import com.uzuu.customer.domain.model.CartItem
import java.text.NumberFormat
import java.util.Locale

class CartItemAdapter(
    private val onCheckedChange: (itemId: Long, checked: Boolean) -> Unit,
    private val onDelete: (itemId: Long) -> Unit,
    private val onQuantityChange: (itemId: Long, newQty: Int) -> Unit
) : ListAdapter<CartItem, CartItemAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<CartItem>() {
            override fun areItemsTheSame(old: CartItem, new: CartItem) = old.id == new.id
            override fun areContentsTheSame(old: CartItem, new: CartItem) = old == new
        }
        private val fmt = NumberFormat.getNumberInstance(Locale("vi", "VN"))
    }

    /** Set id đang được chọn — cập nhật từ Fragment khi state thay đổi */
    var selectedIds: Set<Long> = emptySet()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class VH(val binding: ItemCartItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemCartItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            tvEventName.text   = item.eventName
            tvTicketType.text  = item.ticketTypeName
            tvUnitPrice.text   = "${fmt.format(item.unitPrice.toLong())}đ / vé"
            tvQuantity.text    = item.quantity.toString()
            tvSubtotal.text    = "${fmt.format(item.subtotal.toLong())}đ"

            // Checkbox chọn để thanh toán / xóa
            checkboxSelect.setOnCheckedChangeListener(null)      // tránh vòng lặp
            checkboxSelect.isChecked = item.id in selectedIds
            checkboxSelect.setOnCheckedChangeListener { _, checked ->
                onCheckedChange(item.id, checked)
            }

            // Nút xóa riêng lẻ
            btnDelete.setOnClickListener { onDelete(item.id) }

            // Tăng / giảm số lượng
            btnMinus.setOnClickListener {
                onQuantityChange(item.id, item.quantity - 1)
            }
            btnPlus.setOnClickListener {
                onQuantityChange(item.id, item.quantity + 1)
            }
        }
    }
}