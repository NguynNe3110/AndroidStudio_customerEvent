package com.uzuu.customer.domain.repository

import com.uzuu.customer.core.result.ApiResult
import com.uzuu.customer.domain.model.Cart

interface CartRepository {
    suspend fun addToCart(ticketTypeId: Long, quantity: Int): ApiResult<Cart>
    suspend fun getCart(): ApiResult<Cart>
    suspend fun clearCart(): ApiResult<Unit>
}