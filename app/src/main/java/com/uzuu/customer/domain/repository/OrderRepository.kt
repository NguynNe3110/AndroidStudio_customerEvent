package com.uzuu.customer.domain.repository

import com.uzuu.customer.core.result.ApiResult
import com.uzuu.customer.domain.model.Order
import com.uzuu.customer.domain.model.PagedResult

interface OrderRepository {
    suspend fun checkout(paymentMethod: String): ApiResult<Order>
    suspend fun getMyOrders(page: Int): ApiResult<PagedResult<Order>>
}