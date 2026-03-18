package com.uzuu.customer.data.remote.dto.response

data class EventResponseDto(
    val id: Long,
    val name: String,
    val categoryName: String,
    val organizerName: String,
    val location: String,
    val startTime: String,
    val endTime: String,
    val saleStartDate: String,
    val saleEndDate: String,
    val description: String,
    val status: String,
    val imageUrls: List<String>,
    val ticketTypes: List<CategoryTicketResponseDto>,
    val createdAt: String,
    val updatedAt: String
)
