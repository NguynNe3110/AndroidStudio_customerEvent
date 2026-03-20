package com.uzuu.customer.data.remote.api

import com.uzuu.customer.data.remote.dto.BaseResponseDto
import com.uzuu.customer.data.remote.dto.response.OrderResponseDto
import com.uzuu.customer.data.remote.dto.response.PageResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface OrderApi {

    @POST("orders/checkout")
    suspend fun checkout(
        @Query("paymentMethod") paymentMethod: String
    ): BaseResponseDto<OrderResponseDto>

    @GET("orders")
    suspend fun getMyOrders(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): BaseResponseDto<PageResponse<OrderResponseDto>>
}