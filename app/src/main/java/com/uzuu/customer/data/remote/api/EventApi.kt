package com.uzuu.customer.data.remote.api

import com.uzuu.customer.data.remote.dto.BaseResponseDto
import com.uzuu.customer.data.remote.dto.response.EventResponseDto
import com.uzuu.customer.data.remote.dto.response.PageResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface EventApi {
    @GET("events")
    suspend fun getEvents(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): BaseResponseDto<PageResponse<EventResponseDto>>
}