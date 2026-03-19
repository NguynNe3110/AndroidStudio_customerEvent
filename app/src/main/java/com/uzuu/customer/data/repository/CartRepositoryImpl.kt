package com.uzuu.customer.data.repository

import com.uzuu.customer.core.result.ApiResult
import com.uzuu.customer.core.result.safeApiCall
import com.uzuu.customer.data.mapper.toDomain
import com.uzuu.customer.data.remote.datasource.CartRemoteDataSource
import com.uzuu.customer.data.remote.dto.response.CartItemResponseDto
import com.uzuu.customer.data.remote.dto.response.CartResponseDto
import com.uzuu.customer.domain.model.Cart
import com.uzuu.customer.domain.model.CartItem
import com.uzuu.customer.domain.repository.CartRepository

class CartRepositoryImpl(
    private val remote: CartRemoteDataSource
) : CartRepository {

    override suspend fun addToCart(ticketTypeId: Long, quantity: Int): ApiResult<Cart> =
        safeApiCall {
            val response = remote.addToCart(ticketTypeId, quantity)
            if (response.code == 200 || response.code == 0 || response.code == 1000) {
                response.result.toDomain()
            } else {
                throw Exception(response.message ?: "Thêm vào giỏ thất bại")
            }
        }

    override suspend fun getCart(): ApiResult<Cart> =
        safeApiCall {
            val response = remote.getCart()
            if (response.code == 200 || response.code == 0 || response.code == 1000) {
                response.result.toDomain()
            } else {
                throw Exception(response.message ?: "Không lấy được giỏ hàng")
            }
        }

    override suspend fun clearCart(): ApiResult<Unit> =
        safeApiCall {
            val response = remote.clearCart()
            if (response.code == 200 || response.code == 0 || response.code == 1000) {
                Unit
            } else {
                throw Exception(response.message ?: "Xóa giỏ thất bại")
            }
        }


}