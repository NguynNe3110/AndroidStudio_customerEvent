package com.uzuu.customer.domain.model

data class PagedResult<T>(
    val data: List<T>,
    val page: Int,
    val totalPages: Int,
    val totalElements: Long,
    val isLast: Boolean
)