package com.uzuu.customer.domain.model

data class Order(
    val id: Long,
    val totalAmount: Double,
    val paymentMethod: String,
    val paymentStatus: String,
    val orderStatus: String,
    val orderDate: String
)