package com.uzuu.customer.feature.middle.cart

import com.uzuu.customer.domain.model.CartItem

data class CartUiState(
    val isLoading: Boolean = false,
    val items: List<CartItem> = emptyList(),
    val totalAmount: Double = 0.0,
    val selectedPayment: String = "BANKING",
    /** ID các CartItem đang được tick chọn */
    val selectedItemIds: Set<Long> = emptySet()
) {
    val selectedItems: List<CartItem>
        get() = items.filter { it.id in selectedItemIds }

    val selectedTotal: Double
        get() = selectedItems.sumOf { it.subtotal }

    val hasSelection: Boolean
        get() = selectedItemIds.isNotEmpty()

    val isAllSelected: Boolean
        get() = items.isNotEmpty() && selectedItemIds.size == items.size
}