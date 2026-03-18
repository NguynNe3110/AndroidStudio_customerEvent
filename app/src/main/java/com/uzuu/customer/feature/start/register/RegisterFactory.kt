package com.uzuu.customer.feature.start.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uzuu.customer.domain.repository.AuthRepository
import com.uzuu.customer.domain.repository.UserRepository

class RegisterFactory(
    private val r : AuthRepository,
    private val ra : UserRepository
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(
                r, ra
            ) as T
        }
        throw IllegalArgumentException("unknow viewmodel class")
    }
}