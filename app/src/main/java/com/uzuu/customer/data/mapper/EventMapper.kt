package com.uzuu.customer.data.mapper

import com.uzuu.customer.data.remote.dto.response.EventResponseDto
import com.uzuu.customer.domain.model.Event

fun EventResponseDto.eventDtoToDomain() : Event {
    return Event(
        id = id,
        name = name,
        categoryName = categoryName,
        location = location,
        startTime = startTime,
        endTime = endTime,
        saleStartDate = saleStartDate,
        saleEndDate = saleEndDate,
        description = description,
        status = status,
        imageUrls = imageUrls,
        ticketTypes = ticketTypes.map { it.ticketDtoToDomain() }
    )
}