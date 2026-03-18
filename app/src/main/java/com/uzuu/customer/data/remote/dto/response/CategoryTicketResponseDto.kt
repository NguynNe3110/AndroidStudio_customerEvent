package com.uzuu.customer.data.remote.dto.response

data class CategoryTicketResponseDto (
    val id: Long,
    val name: String,
    val price: Double,
    val totalQuantity: Int,
    val remainingQuantity: Int,
    val description: String
)