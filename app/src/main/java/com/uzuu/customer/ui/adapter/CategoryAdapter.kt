package com.uzuu.customer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uzuu.customer.R
import com.uzuu.customer.databinding.ItemCategoryInHomeBinding
import com.uzuu.customer.domain.model.CategoryItem

class CategoryAdapter(
    private val onClick: (CategoryItem) -> Unit
) : ListAdapter<CategoryItem, CategoryAdapter.ViewHolder>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<CategoryItem>() {
            override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem) =
                oldItem == newItem
        }
    }

    inner class ViewHolder(val binding: ItemCategoryInHomeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryInHomeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.tvNameCategory.text = item.name

        // Highlight danh mục đang chọn
        holder.binding.root.setBackgroundResource(
            if (item.isSelected) R.drawable.bg_category_selected
            else R.drawable.bg_category_item
        )

        holder.binding.root.setOnClickListener {
            onClick(item)
        }
    }
}