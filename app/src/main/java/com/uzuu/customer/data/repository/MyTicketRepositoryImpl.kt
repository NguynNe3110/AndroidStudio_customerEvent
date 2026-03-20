package com.uzuu.customer.data.repository

import com.uzuu.customer.core.result.ApiResult
import com.uzuu.customer.core.result.safeApiCall
import com.uzuu.customer.data.remote.datasource.MyTicketRemoteDataSource
import com.uzuu.customer.domain.model.MyTicket
import com.uzuu.customer.domain.repository.MyTicketRepository

class MyTicketRepositoryImpl(
    private val remote: MyTicketRemoteDataSource
) : MyTicketRepository {

    override suspend fun getMyTickets(): ApiResult<List<MyTicket>> =
        safeApiCall {
            val response = remote.getMyTickets()
            if (response.code == 200 || response.code == 0 || response.code == 1000) {
                response.result.map { dto ->
                    MyTicket(
                        id             = dto.id,
                        eventName      = dto.eventName,
                        ticketTypeName = dto.ticketTypeName,
                        ticketCode     = dto.ticketCode,
                        qrCode         = dto.qrCode,
                        status         = dto.status,
                        usedAt         = dto.usedAt
                    )
                }
            } else {
                throw Exception(response.message ?: "Không lấy được danh sách vé")
            }
        }
}