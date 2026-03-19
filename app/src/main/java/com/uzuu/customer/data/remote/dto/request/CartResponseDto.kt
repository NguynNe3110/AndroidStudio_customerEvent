package com.uzuu.customer.data.remote.dto.response

data class CartResponseDto(
    val id: Long,
    val items: List<CartItemResponseDto>,
    val totalAmount: Double
)