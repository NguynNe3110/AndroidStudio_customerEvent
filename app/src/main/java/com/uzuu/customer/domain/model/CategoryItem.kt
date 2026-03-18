package com.uzuu.customer.domain.model

data class CategoryItem(
    val id: Int,
    val name: String,
    val isSelected: Boolean = false
)