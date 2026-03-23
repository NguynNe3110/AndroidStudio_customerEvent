package com.uzuu.admin.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uzuu.admin.domain.repository.AuthRepository

class LoginFactory(
    private val authRepo: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(authRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}