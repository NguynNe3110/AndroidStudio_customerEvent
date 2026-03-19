package com.uzuu.customer.feature.middle.cart

import com.uzuu.customer.domain.model.CartItem

data class CartUiState(
    val isLoading: Boolean = false,
    val items: List<CartItem> = emptyList(),
    val totalAmount: Double = 0.0,
    val selectedPayment: String = "BANKING"   // MOMO | VNPAY | BANKING
)