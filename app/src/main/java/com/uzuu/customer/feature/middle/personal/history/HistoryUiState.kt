package com.uzuu.customer.feature.middle.personal.history

import com.uzuu.customer.domain.model.Order

data class HistoryUiState(
    val isLoading: Boolean = false,
    val orders: List<Order> = emptyList(),
    val isLastPage: Boolean = false
)