package com.uzuu.customer.data.remote.api

import com.uzuu.customer.data.remote.dto.BaseResponseDto
import com.uzuu.customer.data.remote.dto.request.CartAddRequestDto
import com.uzuu.customer.data.remote.dto.response.CartResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface CartApi {

    @POST("cart/add")
    suspend fun addToCart(
        @Body request: CartAddRequestDto
    ): BaseResponseDto<CartResponseDto>

    @GET("cart")
    suspend fun getCart(): BaseResponseDto<CartResponseDto>

    @DELETE("cart/clear")
    suspend fun clearCart(): BaseResponseDto<Any>
}