package com.uzuu.customer.feature.middle.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uzuu.customer.domain.repository.CategoryRepository
import com.uzuu.customer.domain.repository.EventRepository

class HomeFactory(
    private val eventRepo: EventRepository,
    private val categoryRepo: CategoryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(eventRepo, categoryRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}