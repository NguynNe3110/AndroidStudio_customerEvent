package com.uzuu.admin.feature.scan

import com.uzuu.admin.domain.model.CheckInResult

// ── State ─────────────────────────────────────────────────────────────────────
data class ScanUiState(
    val isLoading: Boolean       = false,
    val username: String         = "",
    // Kết quả check-in gần nhất để hiển thị trên màn hình
    val lastResult: CheckInResult? = null,
    // trạng thái: IDLE | SUCCESS | FAIL
    val resultStatus: ResultStatus = ResultStatus.IDLE,
    // Cho phép quét tiếp hay không (tránh double-scan)
    val isScanEnabled: Boolean   = true
)

enum class ResultStatus { IDLE, SUCCESS, FAIL }

// ── Event (one-shot) ──────────────────────────────────────────────────────────
sealed class ScanUiEvent {
    data class Toast(val message: String) : ScanUiEvent()
    object NavigateToLogin : ScanUiEvent()
}