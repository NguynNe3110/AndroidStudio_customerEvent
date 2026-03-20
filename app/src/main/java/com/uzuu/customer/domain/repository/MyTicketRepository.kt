package com.uzuu.customer.domain.repository

import com.uzuu.customer.core.result.ApiResult
import com.uzuu.customer.domain.model.MyTicket

interface MyTicketRepository {
    suspend fun getMyTickets(): ApiResult<List<MyTicket>>
}