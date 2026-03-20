package com.uzuu.customer.data.remote.dto.response

data class MyTicketResponseDto(
    val id: Long,
    val eventName: String,
    val ticketTypeName: String,
    val ticketCode: String,
    val qrCode: String,       // base64 QR image trả về từ server
    val status: String,       // VALID | USED | EXPIRED
    val usedAt: String?
)