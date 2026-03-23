package com.uzuu.admin.data.remote.dto.response

data class CheckInResponseDto (
    val id: Long,
    val eventName: String,
    val ticketTypeName: String,
    val ticketCode: String,
    val qrCode: String,
    val status: String,
    val usedAt: String
)