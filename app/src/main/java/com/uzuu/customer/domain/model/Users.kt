package com.uzuu.customer.domain.model

data class Users(
    val id: Int,
    val username: String,
    val password: String,
    val email: String,
    val fullName: String,
    val phoneNumber: String,
    val address: String,
)
