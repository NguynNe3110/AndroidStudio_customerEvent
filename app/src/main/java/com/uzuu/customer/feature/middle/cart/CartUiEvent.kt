package com.uzuu.customer.feature.middle.cart

sealed class CartUiEvent {
    data class Toast(val message: String) : CartUiEvent()
    object CheckoutSuccess : CartUiEvent()
    object CartCleared : CartUiEvent()
}