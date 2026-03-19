package com.uzuu.customer.domain.model

data class Cart(
    val id: Long,
    val items: List<CartItem>,
    val totalAmount: Double
)