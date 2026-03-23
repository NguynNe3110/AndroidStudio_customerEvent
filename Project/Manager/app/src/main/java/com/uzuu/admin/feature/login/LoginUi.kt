package com.uzuu.admin.feature.login

// ── State ─────────────────────────────────────────────────────────────────────
data class LoginUiState(
    val isLoading: Boolean = false,
    val username: String   = ""
)

// ── Event (one-shot) ──────────────────────────────────────────────────────────
sealed class LoginUiEvent {
    data class Toast(val message: String) : LoginUiEvent()
    object NavigateToScan : LoginUiEvent()
}