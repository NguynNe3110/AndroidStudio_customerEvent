package com.uzuu.customer.data.repository

import com.uzuu.customer.core.result.ApiResult
import com.uzuu.customer.core.result.safeApiCall
import com.uzuu.customer.data.mapper.toDomain
import com.uzuu.customer.data.remote.datasource.CartRemoteDataSource
import com.uzuu.customer.domain.model.Cart
import com.uzuu.customer.domain.repository.CartRepository

class CartRepositoryImpl(
    private val remote: CartRemoteDataSource
) : CartRepository {

    private fun isOk(code: Int) = code == 200 || code == 0 || code == 1000

    override suspend fun addToCart(ticketTypeId: Long, quantity: Int): ApiResult<Cart> =
        safeApiCall {
            val r = remote.addToCart(ticketTypeId, quantity)
            if (isOk(r.code)) r.result.toDomain()
            else throw Exception(r.message ?: "Thêm vào giỏ thất bại")
        }

    override suspend fun getCart(): ApiResult<Cart> =
        safeApiCall {
            val r = remote.getCart()
            if (isOk(r.code)) r.result.toDomain()
            else throw Exception(r.message ?: "Không lấy được giỏ hàng")
        }

    override suspend fun clearCart(): ApiResult<Unit> =
        safeApiCall {
            val r = remote.clearCart()
            if (isOk(r.code)) Unit
            else throw Exception(r.message ?: "Xóa giỏ thất bại")
        }

    override suspend fun updateCartItem(itemId: Long, quantity: Int): ApiResult<Cart> =
        safeApiCall {
            val r = remote.updateCartItem(itemId, quantity)
            if (isOk(r.code)) r.result.toDomain()
            else throw Exception(r.message ?: "Cập nhật số lượng thất bại")
        }

    override suspend fun deleteCartItem(itemId: Long): ApiResult<Cart> =
        safeApiCall {
            val r = remote.deleteCartItem(itemId)
            if (isOk(r.code)) r.result.toDomain()
            else throw Exception(r.message ?: "Xóa mục thất bại")
        }
}