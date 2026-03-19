package com.uzuu.customer.data.remote.dto.request

data class CartAddRequestDto(
    val ticketTypeId: Long,
    val quantity: Int
)