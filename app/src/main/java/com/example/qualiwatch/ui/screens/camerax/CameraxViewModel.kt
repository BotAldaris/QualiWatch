package com.example.qualiwatch.ui.screens.camerax

import android.util.Log
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.qualiwatch.model.ImageResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CameraxUiState(
    val showDialog: Boolean = false,
    val imageResponseList: List<ImageResponse> = listOf()
)

class CameraxViewModel(
    private val imageToText: (
        imageProxy: ImageProxy, num: Int,
        updateList: (List<ImageResponse>) -> Unit
    ) -> Unit,
    private val numInput: Int
) :
    ViewModel() {
    private val _uiState = MutableStateFlow(CameraxUiState())
    val uiState = _uiState.asStateFlow()

    fun onPhotoTaken(imageProxy: ImageProxy) {
        imageToText(imageProxy, numInput, this::updateImageResponseListAndShowDialog)
    }

    fun updateShowDialog(show: Boolean) {
        _uiState.update { it.copy(showDialog = show) }
    }

    private fun updateImageResponseListAndShowDialog(list: List<ImageResponse>) {
        Log.d("saida", list.toString())
        _uiState.update { it.copy(showDialog = true, imageResponseList = list) }
    }
}

class CameraxViewModelFactory(
    private val imageToText: (
        imageProxy: ImageProxy, num: Int,
        updateList: (List<ImageResponse>) -> Unit
    ) -> Unit,
    private val numInput: Int
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CameraxViewModel::class.java)) {
            return CameraxViewModel(
                imageToText,
                numInput
            ) as T
        }
        throw IllegalArgumentException("ViewModel class not found.")
    }
}