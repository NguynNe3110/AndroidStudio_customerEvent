package com.uzuu.customer.data.remote.dto.response

data class PageResponse<T>(
    val content: List<T>,
    val totalElements: Long,
    val totalPages: Int,
    val size: Int,
    val number: Int, // page hiện tại
    val first: Boolean,
    val last: Boolean,
    val empty: Boolean
)