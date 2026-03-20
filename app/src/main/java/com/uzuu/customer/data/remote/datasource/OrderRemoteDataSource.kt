package com.uzuu.customer.data.remote.datasource

import com.uzuu.customer.data.remote.api.OrderApi
import com.uzuu.customer.data.remote.dto.BaseResponseDto
import com.uzuu.customer.data.remote.dto.response.OrderResponseDto
import com.uzuu.customer.data.remote.dto.response.PageResponse

class OrderRemoteDataSource(
    private val orderApi: OrderApi
) {
    suspend fun checkout(paymentMethod: String): BaseResponseDto<OrderResponseDto> {
        return orderApi.checkout(paymentMethod)
    }

    suspend fun getMyOrders(page: Int): BaseResponseDto<PageResponse<OrderResponseDto>> {
        return orderApi.getMyOrders(page, 20)
    }
}