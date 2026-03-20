package com.uzuu.customer.feature.middle.personal

data class PersonalUiState(
    val isLoading: Boolean = false,
    val username: String = "",
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val avatarUri: String? = null   // URI ảnh local (từ gallery)
)