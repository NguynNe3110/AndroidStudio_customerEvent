package com.uzuu.customer.data.remote.dto.response

data class EventResponseDto(
    val id: Long,
    val name: String,
    val categoryName: String,
    val organizerName: String?,   // ← thêm ?
    val location: String,
    val startTime: String?,       // ← thêm ?
    val endTime: String?,         // ← thêm ?
    val saleStartDate: String?,   // ← thêm ?
    val saleEndDate: String?,     // ← thêm ?
    val description: String?,     // ← thêm ?
    val status: String,
    val imageUrls: List<String>,
    val ticketTypes: List<CategoryTicketResponseDto>,
    val createdAt: String?,       // ← thêm ?
    val updatedAt: String?        // ← thêm ?
)