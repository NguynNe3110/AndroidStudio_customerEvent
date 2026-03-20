package com.uzuu.customer.feature.middle.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uzuu.customer.core.result.ApiResult
import com.uzuu.customer.domain.repository.MyTicketRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TicketViewModel(
    private val myTicketRepo: MyTicketRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TicketUiState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<TicketUiEvent>(extraBufferCapacity = 3)
    val event = _event.asSharedFlow()

    fun loadTickets() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = myTicketRepo.getMyTickets()) {
                is ApiResult.Success -> {
                    _state.update { it.copy(isLoading = false, tickets = result.data) }
                }
                is ApiResult.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _event.emit(TicketUiEvent.Toast(result.message))
                }
            }
        }
    }
}