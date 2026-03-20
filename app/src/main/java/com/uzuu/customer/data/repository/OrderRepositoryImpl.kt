package com.uzuu.customer.data.repository

import com.uzuu.customer.core.result.ApiResult
import com.uzuu.customer.core.result.safeApiCall
import com.uzuu.customer.data.remote.datasource.OrderRemoteDataSource
import com.uzuu.customer.domain.model.Order
import com.uzuu.customer.domain.model.PagedResult
import com.uzuu.customer.domain.repository.OrderRepository

class OrderRepositoryImpl(
    private val remote: OrderRemoteDataSource
) : OrderRepository {

    override suspend fun checkout(paymentMethod: String): ApiResult<Order> =
        safeApiCall {
            val response = remote.checkout(paymentMethod)
            if (response.code == 200 || response.code == 0 || response.code == 1000) {
                response.result.toDomain()
            } else {
                throw Exception(response.message ?: "Thanh toán thất bại")
            }
        }

    override suspend fun getMyOrders(page: Int): ApiResult<PagedResult<Order>> =
        safeApiCall {
            val response = remote.getMyOrders(page)
            if (response.code == 200 || response.code == 0 || response.code == 1000) {
                val pageData = response.result
                PagedResult(
                    data          = pageData.content.map { it.toDomain() },
                    page          = pageData.number,
                    totalPages    = pageData.totalPages,
                    totalElements = pageData.totalElements,
                    isLast        = pageData.last
                )
            } else {
                throw Exception(response.message ?: "Không lấy được lịch sử đơn hàng")
            }
        }
}

// Extension nhỏ gọn để map DTO → Domain (dùng nội bộ file này)
private fun com.uzuu.customer.data.remote.dto.response.OrderResponseDto.toDomain() = Order(
    id            = id,
    totalAmount   = totalAmount,
    paymentMethod = paymentMethod,
    paymentStatus = paymentStatus,
    orderStatus   = orderStatus,
    orderDate     = orderDate
)