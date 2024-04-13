package com.example.qualiwatch.ui.screens.shared

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SharedCameraxAddUiState(
    val imageProxy: String? = null
)

class SharedCameraxAddViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SharedCameraxAddUiState())
    val uiState: StateFlow<SharedCameraxAddUiState> = _uiState.asStateFlow()

    fun updateImageProxy(imageProxy: String?) {
        _uiState.update { it.copy(imageProxy = imageProxy) }
    }
}