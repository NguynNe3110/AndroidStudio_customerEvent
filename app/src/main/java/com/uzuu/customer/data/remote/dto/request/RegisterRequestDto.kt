package com.uzuu.customer.data.remote.dto.request

data class RegisterRequestDto(
    val username: String,
    val password: String,
    val email: String,
    val fullName: String,
    val phone: String,
    val address: String,
    val role : String = "CUSTOMER"
)