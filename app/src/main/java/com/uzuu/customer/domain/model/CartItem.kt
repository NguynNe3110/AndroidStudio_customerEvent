package com.uzuu.customer.domain.model

data class CartItem(
    val id: Long,
    val ticketTypeId: Long,
    val ticketTypeName: String,
    val eventName: String,
    val quantity: Int,
    val unitPrice: Double,
    val subtotal: Double
)