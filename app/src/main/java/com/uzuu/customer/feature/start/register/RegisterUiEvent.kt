package com.uzuu.customer.feature.start.register

sealed class RegisterUiEvent {

    data class Toast(val message: String) : RegisterUiEvent()

    data class navigateWith(val id: Int) : RegisterUiEvent()

    data class navigateToLogin(val user: String) : RegisterUiEvent()

    object Loading : RegisterUiEvent()
    object Success  : RegisterUiEvent()
    object Error : RegisterUiEvent()
}