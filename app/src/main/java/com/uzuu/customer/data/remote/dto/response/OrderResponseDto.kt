package com.uzuu.customer.data.remote.dto.response

data class OrderResponseDto(
    val id: Long,
    val totalAmount: Double,
    val paymentMethod: String,
    val paymentStatus: String,   // PENDING | PAID | FAILED
    val orderStatus: String,     // PENDING | CONFIRMED | CANCELLED
    val orderDate: String
)