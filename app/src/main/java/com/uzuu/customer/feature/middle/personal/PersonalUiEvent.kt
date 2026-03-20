package com.uzuu.customer.feature.middle.personal

sealed class PersonalUiEvent {
    data class Toast(val message: String) : PersonalUiEvent()
    object NavigateToEditInfo : PersonalUiEvent()
    object NavigateToLogin : PersonalUiEvent()
}