package com.uzuu.customer.feature.middle.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uzuu.customer.domain.repository.CartRepository

class CartFactory(
    private val cartRepo: CartRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CartViewModel(cartRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}