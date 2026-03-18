package com.uzuu.customer.data.remote.datasource

import com.uzuu.customer.data.remote.api.EventApi
import com.uzuu.customer.data.remote.dto.BaseResponseDto
import com.uzuu.customer.data.remote.dto.response.EventResponseDto
import com.uzuu.customer.data.remote.dto.response.PageResponse

class EventRemoteDataSource(
    private val eventApi: EventApi
) {
    suspend fun getEvent(page: Int):  BaseResponseDto<PageResponse<EventResponseDto>> {
        return eventApi.getEvents(page, 10)
    }
}