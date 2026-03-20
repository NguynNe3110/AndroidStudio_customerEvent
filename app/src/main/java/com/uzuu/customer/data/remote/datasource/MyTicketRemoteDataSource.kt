package com.uzuu.customer.data.remote.datasource

import com.uzuu.customer.data.remote.api.MyTicketApi
import com.uzuu.customer.data.remote.dto.BaseResponseDto
import com.uzuu.customer.data.remote.dto.response.MyTicketResponseDto

class MyTicketRemoteDataSource(
    private val myTicketApi: MyTicketApi
) {
    suspend fun getMyTickets(): BaseResponseDto<List<MyTicketResponseDto>> {
        return myTicketApi.getMyTickets()
    }
}