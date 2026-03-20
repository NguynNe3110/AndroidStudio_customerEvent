package com.uzuu.customer.feature.middle.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uzuu.customer.core.result.ApiResult
import com.uzuu.customer.domain.repository.CartRepository
import com.uzuu.customer.domain.repository.OrderRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepo: CartRepository,
    private val orderRepo: OrderRepository      // ← thêm
) : ViewModel() {

    private val _cartState = MutableStateFlow(CartUiState())
    val cartState = _cartState.asStateFlow()

    private val _cartEvent = MutableSharedFlow<CartUiEvent>(extraBufferCapacity = 3)
    val cartEvent = _cartEvent.asSharedFlow()

    fun loadCart() {
        viewModelScope.launch {
            _cartState.update { it.copy(isLoading = true) }
            when (val result = cartRepo.getCart()) {
                is ApiResult.Success -> {
                    val cart = result.data
                    _cartState.update {
                        it.copy(
                            isLoading   = false,
                            items       = cart.items,
                            totalAmount = cart.totalAmount
                        )
                    }
                }
                is ApiResult.Error -> {
                    _cartState.update { it.copy(isLoading = false) }
                    _cartEvent.emit(CartUiEvent.Toast(result.message))
                }
            }
        }
    }

    fun onPaymentSelected(method: String) {
        _cartState.update { it.copy(selectedPayment = method) }
    }

    fun onClearCart() {
        viewModelScope.launch {
            _cartState.update { it.copy(isLoading = true) }
            when (val result = cartRepo.clearCart()) {
                is ApiResult.Success -> {
                    _cartState.update {
                        it.copy(isLoading = false, items = emptyList(), totalAmount = 0.0)
                    }
                    _cartEvent.emit(CartUiEvent.CartCleared)
                    _cartEvent.emit(CartUiEvent.Toast("Đã xóa toàn bộ giỏ hàng"))
                }
                is ApiResult.Error -> {
                    _cartState.update { it.copy(isLoading = false) }
                    _cartEvent.emit(CartUiEvent.Toast(result.message))
                }
            }
        }
    }

    fun onCheckout() {
        val state = _cartState.value
        if (state.items.isEmpty()) {
            viewModelScope.launch {
                _cartEvent.emit(CartUiEvent.Toast("Giỏ hàng đang trống"))
            }
            return
        }
        viewModelScope.launch {
            _cartState.update { it.copy(isLoading = true) }

            // Gọi POST /orders/checkout?paymentMethod=...
            when (val result = orderRepo.checkout(state.selectedPayment)) {
                is ApiResult.Success -> {
                    _cartState.update { it.copy(isLoading = false) }
                    _cartEvent.emit(CartUiEvent.Toast("🎉 Đặt hàng thành công!"))
                    _cartEvent.emit(CartUiEvent.CheckoutSuccess)
                    // CheckoutSuccess → CartFragment gọi loadCart()
                    // Server đã clear cart sau checkout nên list sẽ rỗng
                }
                is ApiResult.Error -> {
                    _cartState.update { it.copy(isLoading = false) }
                    _cartEvent.emit(CartUiEvent.Toast(result.message))
                }
            }
        }
    }
}