package com.uzuu.customer.feature.middle.ticket

sealed class TicketUiEvent {
    data class Toast(val message: String) : TicketUiEvent()
}