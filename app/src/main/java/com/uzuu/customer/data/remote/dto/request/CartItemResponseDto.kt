package com.uzuu.customer.data.remote.dto.response

data class CartItemResponseDto(
    val id: Long,
    val ticketTypeId: Long,
    val ticketTypeName: String,
    val eventName: String,
    val quantity: Int,
    val unitPrice: Double,
    val subtotal: Double
)