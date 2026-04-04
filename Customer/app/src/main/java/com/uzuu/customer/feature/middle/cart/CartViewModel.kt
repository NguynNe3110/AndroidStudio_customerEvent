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
    private val orderRepo: OrderRepository
) : ViewModel() {

    private val _cartState = MutableStateFlow(CartUiState())
    val cartState = _cartState.asStateFlow()

    private val _cartEvent = MutableSharedFlow<CartUiEvent>(extraBufferCapacity = 3)
    val cartEvent = _cartEvent.asSharedFlow()

    // ── Load ─────────────────────────────────────────────────────────────────
    fun loadCart() {
        viewModelScope.launch {
            _cartState.update { it.copy(isLoading = true) }
            when (val r = cartRepo.getCart()) {
                is ApiResult.Success -> {
                    val cart = r.data
                    _cartState.update {
                        it.copy(
                            isLoading = false,
                            items = cart.items,
                            totalAmount = cart.totalAmount,
                            // Bỏ các id đã chọn mà không còn tồn tại
                            selectedItemIds = it.selectedItemIds
                                .filter { id -> cart.items.any { item -> item.id == id } }
                                .toSet()
                        )
                    }
                }
                is ApiResult.Error -> {
                    _cartState.update { it.copy(isLoading = false) }
                    _cartEvent.emit(CartUiEvent.Toast(r.message))
                }
            }
        }
    }

    // ── Selection ─────────────────────────────────────────────────────────────
    fun toggleItemSelection(itemId: Long) {
        _cartState.update { state ->
            val newSet = state.selectedItemIds.toMutableSet()
            if (itemId in newSet) newSet.remove(itemId) else newSet.add(itemId)
            state.copy(selectedItemIds = newSet)
        }
    }

    fun toggleSelectAll() {
        _cartState.update { state ->
            val newSet = if (state.isAllSelected) emptySet()
            else state.items.map { it.id }.toSet()
            state.copy(selectedItemIds = newSet)
        }
    }

    // ── Update quantity ───────────────────────────────────────────────────────
    fun updateItemQuantity(itemId: Long, quantity: Int) {
        if (quantity <= 0) {
            return
        }
        viewModelScope.launch {
            when (val r = cartRepo.updateCartItem(itemId, quantity)) {
                is ApiResult.Success -> {
                    val cart = r.data
                    _cartState.update {
                        it.copy(items = cart.items, totalAmount = cart.totalAmount)
                    }
                }
                is ApiResult.Error -> _cartEvent.emit(CartUiEvent.Toast(r.message))
            }
        }
    }

    // ── Delete selected items ─────────────────────────────────────────────────
    fun deleteSelectedItems() {
        val ids = _cartState.value.selectedItemIds
        if (ids.isEmpty()) {
            viewModelScope.launch { _cartEvent.emit(CartUiEvent.Toast("Chưa chọn mục nào")) }
            return
        }
        viewModelScope.launch {
            _cartState.update { it.copy(isLoading = true) }
            var lastCart = _cartState.value.items
            var lastTotal = _cartState.value.totalAmount
            var hasError = false

            ids.forEach { itemId ->
                when (val r = cartRepo.deleteCartItem(itemId)) {
                    is ApiResult.Success -> {
                        lastCart  = r.data.items
                        lastTotal = r.data.totalAmount
                    }
                    is ApiResult.Error -> hasError = true
                }
            }

            _cartState.update {
                it.copy(
                    isLoading = false,
                    items = lastCart,
                    totalAmount = lastTotal,
                    selectedItemIds = emptySet()
                )
            }

            if (hasError) _cartEvent.emit(CartUiEvent.Toast("Có lỗi khi xóa, vui lòng thử lại"))
            else _cartEvent.emit(CartUiEvent.Toast("Đã xóa ${ids.size} mục khỏi giỏ"))
        }
    }

    // ── Clear all ─────────────────────────────────────────────────────────────
    fun onClearCart() {
        viewModelScope.launch {
            _cartState.update { it.copy(isLoading = true) }
            when (val r = cartRepo.clearCart()) {
                is ApiResult.Success -> {
                    _cartState.update {
                        it.copy(isLoading = false, items = emptyList(),
                            totalAmount = 0.0, selectedItemIds = emptySet())
                    }
                    _cartEvent.emit(CartUiEvent.CartCleared)
                    _cartEvent.emit(CartUiEvent.Toast("Đã xóa toàn bộ giỏ hàng"))
                }
                is ApiResult.Error -> {
                    _cartState.update { it.copy(isLoading = false) }
                    _cartEvent.emit(CartUiEvent.Toast(r.message))
                }
            }
        }
    }

    // ── Payment method ────────────────────────────────────────────────────────
    fun onPaymentSelected(method: String) {
        _cartState.update { it.copy(selectedPayment = method) }
    }

    // ── Checkout all ──────────────────────────────────────────────────────────
    fun onCheckout() {
        val state = _cartState.value
        if (state.items.isEmpty()) {
            viewModelScope.launch { _cartEvent.emit(CartUiEvent.Toast("Giỏ hàng đang trống")) }
            return
        }
        viewModelScope.launch {
            _cartState.update { it.copy(isLoading = true) }
            when (val r = orderRepo.checkout(state.selectedPayment)) {
                is ApiResult.Success -> {
                    _cartState.update { it.copy(isLoading = false) }
                    _cartEvent.emit(CartUiEvent.Toast("🎉 Đặt hàng thành công!"))
                    _cartEvent.emit(CartUiEvent.CheckoutSuccess)
                }
                is ApiResult.Error -> {
                    _cartState.update { it.copy(isLoading = false) }
                    _cartEvent.emit(CartUiEvent.Toast(r.message))
                }
            }
        }
    }

    // ── Checkout selected ─────────────────────────────────────────────────────
    fun onCheckoutSelected() {
        val state = _cartState.value
        if (!state.hasSelection) {
            viewModelScope.launch { _cartEvent.emit(CartUiEvent.Toast("Chưa chọn mục nào")) }
            return
        }
        val itemIds = state.selectedItemIds.toList()
        viewModelScope.launch {
            _cartState.update { it.copy(isLoading = true) }
            when (val r = orderRepo.checkoutSelected(state.selectedPayment, itemIds)) {
                is ApiResult.Success -> {
                    _cartState.update {
                        it.copy(isLoading = false, selectedItemIds = emptySet())
                    }
                    _cartEvent.emit(CartUiEvent.Toast("🎉 Đặt hàng ${itemIds.size} mục thành công!"))
                    _cartEvent.emit(CartUiEvent.CheckoutSuccess)
                }
                is ApiResult.Error -> {
                    _cartState.update { it.copy(isLoading = false) }
                    _cartEvent.emit(CartUiEvent.Toast(r.message))
                }
            }
        }
    }
}