package com.uzuu.customer.feature.start.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uzuu.customer.core.result.ApiResult
import com.uzuu.customer.data.session.SessionManager
import com.uzuu.customer.domain.model.Login
import com.uzuu.customer.domain.repository.AuthRepository
import com.uzuu.customer.domain.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepo: AuthRepository,
    private val userRepo: UserRepository

): ViewModel() {

    private val _loginState = MutableStateFlow(LoginUiState())
    val loginState = _loginState.asStateFlow()

    private val _loginEvent = MutableSharedFlow<LoginUiEvent>(extraBufferCapacity = 3)
    val loginEvent = _loginEvent.asSharedFlow()


    fun onClickForgetPass(user: String) {
        _loginEvent.tryEmit(LoginUiEvent.navigateForget(user))
    }

    fun onClickRegister() {
        _loginEvent.tryEmit(LoginUiEvent.navigateRegister)
    }

    fun observe(username: String) {
        _loginState.update { it.copy(username = username) }
    }

    fun onClickLogin(
        user: String,
        pass: String
    ){
        viewModelScope.launch {
            _loginState.update { it.copy(isLoading = true) }
            delay(400)

            if (user.isBlank() || pass.isBlank()) {
                _loginState.update { it.copy(isLoading = false) } // ← thêm dòng này
                _loginEvent.tryEmit(LoginUiEvent.Toast("Thông tin không được để trống"))
                return@launch
            }

            val login = Login(user, pass)

            when(val r = authRepo.loginRequest(login)) {

                is ApiResult.Success -> {
                    println("DEBUG [LoginVM] response = ${r.data}")
                    println("DEBUG [LoginVM] new token = ${r.data.result.token}")

                    val token = r.data.result.token
                    SessionManager.saveToken(token)
                    println("DEBUG [LoginVM] saved token = ${SessionManager.getToken()}")
                    _loginState.update { it.copy(isLoading = false) }
                    _loginEvent.tryEmit(LoginUiEvent.Toast("Đăng nhập thành công"))
                    _loginEvent.tryEmit(LoginUiEvent.navigateHome)
                }

                is ApiResult.Error -> {
                    println("DEBUG [LoginVM] login FAILED: ${r.message}")
                    _loginState.update { it.copy(isLoading = false) }
                    _loginEvent.tryEmit(LoginUiEvent.Toast(r.message))
                }
            }
        }

    }
}