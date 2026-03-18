package com.uzuu.customer.data.mapper

import com.uzuu.customer.data.remote.dto.response.CategoryTicketResponseDto
import com.uzuu.customer.domain.model.CategoryTicket

fun CategoryTicketResponseDto.ticketDtoToDomain() : CategoryTicket{
    return CategoryTicket(
        id = id,
        name = name,
        price = price,
        totalQuantity = totalQuantity,
        remainingQuantity = remainingQuantity
    )
}