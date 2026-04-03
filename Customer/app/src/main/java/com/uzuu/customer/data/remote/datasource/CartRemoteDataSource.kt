package com.uzuu.customer.data.remote.datasource

import com.uzuu.customer.data.remote.api.CartApi
import com.uzuu.customer.data.remote.dto.BaseResponseDto
import com.uzuu.customer.data.remote.dto.request.CartAddRequestDto
import com.uzuu.customer.data.remote.dto.response.CartResponseDto

class CartRemoteDataSource(private val cartApi: CartApi) {

    suspend fun addToCart(ticketTypeId: Long, quantity: Int): BaseResponseDto<CartResponseDto> =
        cartApi.addToCart(CartAddRequestDto(ticketTypeId, quantity))

    suspend fun getCart(): BaseResponseDto<CartResponseDto> =
        cartApi.getCart()

    suspend fun clearCart(): BaseResponseDto<Any> =
        cartApi.clearCart()

    suspend fun updateCartItem(itemId: Long, quantity: Int): BaseResponseDto<CartResponseDto> =
        cartApi.updateCartItem(itemId, quantity)

    suspend fun deleteCartItem(itemId: Long): BaseResponseDto<CartResponseDto> =
        cartApi.deleteCartItem(itemId)
}