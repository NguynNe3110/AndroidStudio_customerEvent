package com.uzuu.customer.data.remote.api

import com.uzuu.customer.data.remote.dto.BaseResponseDto
import com.uzuu.customer.data.remote.dto.response.MyTicketResponseDto
import retrofit2.http.GET

interface MyTicketApi {

    @GET("tickets/my-tickets")
    suspend fun getMyTickets(): BaseResponseDto<List<MyTicketResponseDto>>
}