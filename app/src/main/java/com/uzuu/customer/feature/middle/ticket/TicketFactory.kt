package com.uzuu.customer.feature.middle.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uzuu.customer.domain.repository.MyTicketRepository

class TicketFactory(
    private val myTicketRepo: MyTicketRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TicketViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TicketViewModel(myTicketRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}