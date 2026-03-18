package com.uzuu.customer.feature.start.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uzuu.customer.domain.repository.AuthRepository
import com.uzuu.customer.domain.repository.UserRepository

class LoginFactory(
    private val r: AuthRepository,
    private val ra : UserRepository
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                r, ra
            ) as T
        }
        throw IllegalArgumentException("Unknow Viewmodel Class!")
    }
}