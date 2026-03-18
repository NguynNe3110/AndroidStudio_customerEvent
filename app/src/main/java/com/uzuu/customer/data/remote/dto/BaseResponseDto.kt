package com.uzuu.customer.data.remote.dto

data class BaseResponseDto<T>(
    val code: Int,
    val message: String,
    val result: T
)
