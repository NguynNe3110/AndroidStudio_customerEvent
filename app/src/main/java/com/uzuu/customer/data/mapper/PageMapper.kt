package com.uzuu.customer.data.mapper

import com.uzuu.customer.data.remote.dto.response.EventResponseDto
import com.uzuu.customer.data.remote.dto.response.PageResponse
import com.uzuu.customer.domain.model.Event
import com.uzuu.customer.domain.model.PagedResult

fun PageResponse<EventResponseDto>.pageDtoToDomain(): PagedResult<Event> {
    return PagedResult(
        data = content.map { it.eventDtoToDomain() },
        page = number,
        totalPages = totalPages,
        totalElements = totalElements,
        isLast = last
    )
}