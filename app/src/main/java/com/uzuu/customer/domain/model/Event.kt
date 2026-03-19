package com.uzuu.customer.domain.model

data class Event(
    val id: Long,
    val name: String,
    val categoryName: String,
    val location: String,
    val startTime: String?,       // ← thêm ?
    val endTime: String?,         // ← thêm ?
    val saleStartDate: String?,   // ← thêm ?
    val saleEndDate: String?,     // ← thêm ?
    val description: String?,     // ← thêm ?
    val status: String,
    val imageUrls: List<String>,
    val ticketTypes: List<CategoryTicket>,
)