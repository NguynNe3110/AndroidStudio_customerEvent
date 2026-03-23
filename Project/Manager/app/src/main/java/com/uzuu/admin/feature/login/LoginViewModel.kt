package com.uzuu.admin.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uzuu.admin.core.result.ApiResult
import com.uzuu.admin.data.session.SessionManager
import com.uzuu.admin.domain.model.Login
import com.uzuu.admin.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepo: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<LoginUiEvent>(extraBufferCapacity = 3)
    val event = _event.asSharedFlow()

    fun onLogin(username: String, password: String) {
        viewModelScope.launch {
            // Validate
            if (username.isBlank() || password.isBlank()) {
                _event.emit(LoginUiEvent.Toast("Vui lòng nhập đầy đủ thông tin"))
                return@launch
            }

            _state.update { it.copy(isLoading = true) }
            delay(300) // nhỏ delay UX

            when (val result = authRepo.login(Login(username, password))) {
                is ApiResult.Success -> {
                    val token = result.data
                    SessionManager.saveToken(token)
                    SessionManager.saveUsername(username)
                    println("DEBUG [LoginVM] token saved, user=$username")

                    _state.update { it.copy(isLoading = false, username = username) }
                    _event.emit(LoginUiEvent.Toast("Đăng nhập thành công!"))
                    _event.emit(LoginUiEvent.NavigateToScan)
                }
                is ApiResult.Error -> {
                    println("DEBUG [LoginVM] login error: ${result.message}")
                    _state.update { it.copy(isLoading = false) }
                    _event.emit(LoginUiEvent.Toast(result.message))
                }
            }
        }
    }
}