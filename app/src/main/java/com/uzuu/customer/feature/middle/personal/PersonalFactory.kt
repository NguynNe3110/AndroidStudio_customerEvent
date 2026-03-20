package com.uzuu.customer.feature.middle.personal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uzuu.customer.domain.repository.UserRepository

class PersonalFactory(
    private val userRepo: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PersonalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PersonalViewModel(userRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}