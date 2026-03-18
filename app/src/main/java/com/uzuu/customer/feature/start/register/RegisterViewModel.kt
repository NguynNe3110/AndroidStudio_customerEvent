package com.uzuu.customer.feature.start.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uzuu.customer.core.result.ApiResult
import com.uzuu.customer.domain.model.Register
import com.uzuu.customer.domain.model.Users
import com.uzuu.customer.domain.repository.AuthRepository
import com.uzuu.customer.domain.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepo: AuthRepository,
    private val userRepo: UserRepository
):  ViewModel() {
    private val _registerState = MutableStateFlow(RegisterUiState())
    val registerState = _registerState.asStateFlow()

    private val _registerEvent = MutableSharedFlow<RegisterUiEvent>(replay = 0, extraBufferCapacity = 3)
    val registerEvent = _registerEvent.asSharedFlow()

    fun onClickRegister(
        user: String,
        pass: String,
        email: String,
        disp: String,
        phone: String,
        add: String
    ) {
        println("DEBUG later register(VM): $user, $pass, $email, $disp, $phone, $add")
        viewModelScope.launch {
            _registerState.update { it.copy(isLoading = true) }
            _registerEvent.emit(RegisterUiEvent.Loading)
            println("DEBUG first register(VM): $user, $pass, $email, $disp, $phone, $add")

            delay(400)

            if (user.isBlank() || pass.isBlank() || email.isBlank() || disp.isBlank() || phone.isBlank() || add.isBlank()) {
                _registerEvent.emit(RegisterUiEvent.Toast("Không được để trống"))
                _registerEvent.emit(RegisterUiEvent.Error)
                _registerState.update { it.copy(isLoading = false) }
                println("DEBUG second register(VM): $user, $pass, $email, $disp, $phone, $add")

                return@launch
            }

            val regis = Register(user, pass, email, disp, phone, add)
            when(val r = authRepo.registerRequest(regis)) {

                is ApiResult.Success -> {
                    _registerState.update { it.copy(isLoading = false) }
                    _registerEvent.emit(RegisterUiEvent.Success)
                    _registerEvent.emit(RegisterUiEvent.Toast("Đăng kí thành công!"))
                    println("DEBUG in third register(VM):d")

                    _registerEvent.emit(RegisterUiEvent.Toast("Tự động chuyển hướng sau 1 giây!"))
                    delay(300)
                    _registerEvent.emit(RegisterUiEvent.navigateToLogin(user))
                }
                is ApiResult.Error -> {
                    _registerState.update { it.copy(isLoading = false) }
                    _registerEvent.emit(RegisterUiEvent.Error)
                    println("DEBUG forth register(VM): $user, $pass, $email, $disp, $phone, $add")

                    _registerEvent.emit(RegisterUiEvent.Toast(r.message))
                }
            }
        }
    }
}