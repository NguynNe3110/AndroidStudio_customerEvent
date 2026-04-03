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

    private fun isOk(code: Int) = code == 200 || code == 0 || code == 1000

    override suspend fun checkout(paymentMethod: String): ApiResult<Order> =
        safeApiCall {
            val r = remote.checkout(paymentMethod)
            if (isOk(r.code)) r.result.toDomain()
            else throw Exception(r.message ?: "Thanh toán thất bại")
        }

    override suspend fun checkoutSelected(
        paymentMethod: String,
        itemIds: List<Long>
    ): ApiResult<Order> =
        safeApiCall {
            val r = remote.checkoutSelected(paymentMethod, itemIds)
            if (isOk(r.code)) r.result.toDomain()
            else throw Exception(r.message ?: "Thanh toán thất bại")
        }

    override suspend fun getMyOrders(page: Int): ApiResult<PagedResult<Order>> =
        safeApiCall {
            val r = remote.getMyOrders(page)
            if (isOk(r.code)) {
                val p = r.result
                PagedResult(
                    data          = p.content.map { it.toDomain() },
                    page          = p.number,
                    totalPages    = p.totalPages,
                    totalElements = p.totalElements,
                    isLast        = p.last
                )
            } else throw Exception(r.message ?: "Không lấy được lịch sử đơn hàng")
        }
}

private fun com.uzuu.customer.data.remote.dto.response.OrderResponseDto.toDomain() = Order(
    id            = id,
    totalAmount   = totalAmount,
    paymentMethod = paymentMethod,
    paymentStatus = paymentStatus,
    orderStatus   = orderStatus,
    orderDate     = orderDate
)