package com.uzuu.customer.data.remote.dto.response

data class UserResponseDto (
    val id: Long,
    val username: String,
    val email: String,
    val fullName: String,
    val phone: String,
    val address: String,
    val role: String
)