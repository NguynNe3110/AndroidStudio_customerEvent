package com.uzuu.customer.domain.model

data class CategoryTicket(
    val id: Long,
    val name: String,
    val price: Double,
    val totalQuantity: Int,
    val remainingQuantity: Int
)
