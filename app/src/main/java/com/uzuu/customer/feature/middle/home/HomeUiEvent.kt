package com.uzuu.customer.feature.middle.home

sealed class HomeUiEvent {
    data class Toast(val message: String) : HomeUiEvent()

    object navigateBack : HomeUiEvent()

    data class navigateWith(val id: Int) : HomeUiEvent()
}