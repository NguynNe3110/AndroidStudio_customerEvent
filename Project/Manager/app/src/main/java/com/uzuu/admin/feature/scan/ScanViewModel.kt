package com.uzuu.admin.feature.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uzuu.admin.core.result.ApiResult
import com.uzuu.admin.data.session.SessionManager
import com.uzuu.admin.domain.repository.CheckInRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScanViewModel(
    private val checkInRepo: CheckInRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ScanUiState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<ScanUiEvent>(extraBufferCapacity = 3)
    val event = _event.asSharedFlow()

    // Gọi khi fragment khởi tạo để hiển thị tên người dùng
    fun init() {
        _state.update { it.copy(username = SessionManager.getUsername() ?: "") }
    }

    /**
     * Gọi khi camera quét được QR code.
     * ticketCode là nội dung text bên trong QR.
     */
    fun onQrScanned(ticketCode: String) {
        // Tránh gọi API nhiều lần trong khi đang xử lý
        if (!_state.value.isScanEnabled || _state.value.isLoading) return

        println("DEBUG [ScanVM] QR scanned: $ticketCode")

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, isScanEnabled = false) }

            when (val result = checkInRepo.checkIn(ticketCode)) {
                is ApiResult.Success -> {
                    println("DEBUG [ScanVM] check-in OK: ${result.data}")
                    _state.update {
                        it.copy(
                            isLoading    = false,
                            lastResult   = result.data,
                            resultStatus = ResultStatus.SUCCESS
                        )
                    }
                    // Tự động re-enable quét sau 3 giây
                    delay(3000)
                    _state.update { it.copy(isScanEnabled = true, resultStatus = ResultStatus.IDLE) }
                }

                is ApiResult.Error -> {
                    println("DEBUG [ScanVM] check-in error: ${result.message}")
                    _state.update {
                        it.copy(
                            isLoading    = false,
                            resultStatus = ResultStatus.FAIL
                        )
                    }
                    _event.emit(ScanUiEvent.Toast(result.message))
                    // Re-enable sau 2 giây
                    delay(2000)
                    _state.update { it.copy(isScanEnabled = true, resultStatus = ResultStatus.IDLE) }
                }
            }
        }
    }

    /**
     * Người dùng nhập mã vé thủ công (trường hợp QR hỏng).
     */
    fun onManualCheckIn(ticketCode: String) {
        if (ticketCode.isBlank()) {
            viewModelScope.launch {
                _event.emit(ScanUiEvent.Toast("Vui lòng nhập mã vé"))
            }
            return
        }
        onQrScanned(ticketCode.trim())
    }

    fun onLogout() {
        SessionManager.clear()
        viewModelScope.launch {
            _event.emit(ScanUiEvent.NavigateToLogin)
        }
    }

    /** Reset về trạng thái idle — dùng khi bấm nút "Quét tiếp" */
    fun onResetScan() {
        _state.update {
            it.copy(
                isScanEnabled = true,
                resultStatus  = ResultStatus.IDLE,
                lastResult    = null
            )
        }
    }
}