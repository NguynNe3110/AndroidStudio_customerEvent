package com.uzuu.customer.data.remote.dto.request

data class UserRequestDto (
    val password: String,
    val email: String,
    val fullName: String,
    val phone: String,
    val address: String
)