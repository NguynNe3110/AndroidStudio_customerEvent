package com.uzuu.customer.feature.middle.personal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uzuu.customer.core.result.ApiResult
import com.uzuu.customer.data.remote.dto.request.UserRequestDto
import com.uzuu.customer.data.session.SessionManager
import com.uzuu.customer.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PersonalViewModel(
    private val userRepo: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PersonalUiState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<PersonalUiEvent>(extraBufferCapacity = 3)
    val event = _event.asSharedFlow()

    // Gọi khi fragment start
    fun init() {
        // Load avatar từ local storage ngay lập tức
        _state.update { it.copy(avatarUri = SessionManager.getAvatarUri()) }

        // Load thông tin từ server
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val r = userRepo.getMyInfo()) {
                is ApiResult.Success -> {
                    val u = r.data
                    println("DEBUG[PersonalViewModel] du lieu server trả về $u")
                    // Lưu username để dùng khi update
                    SessionManager.saveUsername(u.username)
                    _state.update {
                        it.copy(
                            isLoading = false,
                            username  = u.username,
                            fullName  = u.fullName,
                            email     = u.email,
                            phone     = u.phone,
                            address   = u.address
                        )
                    }
                }
                is ApiResult.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _event.emit(PersonalUiEvent.Toast(r.message))
                }
            }
        }
    }

    // Lưu avatar URI (từ gallery) vào SharedPreferences + state
    fun saveAvatarUri(uri: String) {
        SessionManager.saveAvatarUri(uri)
        _state.update { it.copy(avatarUri = uri) }
    }

    fun onEditInfo() {
        viewModelScope.launch {
            _event.emit(PersonalUiEvent.NavigateToEditInfo)
        }
    }

    fun updateInfo(password: String, email: String, fullName: String, phone: String, address: String) {
        val username = _state.value.username.ifBlank {
            SessionManager.getUsername() ?: run {
                viewModelScope.launch { _event.emit(PersonalUiEvent.Toast("Không xác định được tài khoản")) }
                return
            }
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val request = UserRequestDto(
                password = password,
                email    = email,
                fullName = fullName,
                phone    = phone,
                address  = address
            )
            when (val r = userRepo.updateInfo(username, request)) {
                is ApiResult.Success -> {
                    val u = r.data
                    _state.update {
                        it.copy(
                            isLoading = false,
                            fullName  = u.fullName,
                            email     = u.email,
                            phone     = u.phone,
                            address   = u.address
                        )
                    }
                    _event.emit(PersonalUiEvent.Toast("Cập nhật thành công!"))
                }
                is ApiResult.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _event.emit(PersonalUiEvent.Toast(r.message))
                }
            }
        }
    }
}