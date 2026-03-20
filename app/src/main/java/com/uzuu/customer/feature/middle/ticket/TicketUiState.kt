package com.uzuu.customer.feature.middle.ticket

import com.uzuu.customer.domain.model.MyTicket

data class TicketUiState(
    val isLoading: Boolean = false,
    val tickets: List<MyTicket> = emptyList()
)