package com.uzuu.learn16_hilt_dependencyinject

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class UiState(
    val name: String = ""
)

@HiltViewModel
class MyViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun getText(): String {
        return repository.getName()
    }

    fun setTextFromInput(data: String) {
        _uiState.update {
            it.copy(name = data)
        }
    }
}
