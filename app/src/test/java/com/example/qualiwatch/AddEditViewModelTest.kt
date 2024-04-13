package com.example.qualiwatch


import androidx.camera.core.ImageProxy
import com.example.qualiwatch.fake.FakeOnOffProductRepository
import com.example.qualiwatch.model.ImageResponse
import com.example.qualiwatch.ui.screens.products.AddEditProductViewModel
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime

class AddEditViewModelTest {
    private fun imageToText(imageProxy: ImageProxy, num: Int): List<ImageResponse> {
        return listOf()
    }

    private val viewModel =
        AddEditProductViewModel(
            FakeOnOffProductRepository(),
            {},
            { imageProxy, num -> imageToText(imageProxy, num) }, null
        )

    @Test
    fun addEditViewModel_onConfirmation_dateWithSeparatorBackSlash() {
        viewModel.updateNumInput(3)
        viewModel.onConfirmation(ImageResponse(3, "30/12/2002"))
        assertEquals(viewModel.uiState.value.expiration, LocalDateTime.of(2002, 12, 30, 0, 0))
    }

    @Test
    fun addEditViewModel_onConfirmation_dateWithSeparatorDash() {
        viewModel.updateNumInput(3)
        viewModel.onConfirmation(ImageResponse(3, "30-12-2002"))
        assertEquals(viewModel.uiState.value.expiration, LocalDateTime.of(2002, 12, 30, 0, 0))
    }

    @Test
    fun addEditViewModel_onConfirmation_dateWithSeparatorDot() {
        viewModel.updateNumInput(3)
        viewModel.onConfirmation(ImageResponse(3, "30.12.2002"))
        assertEquals(viewModel.uiState.value.expiration, LocalDateTime.of(2002, 12, 30, 0, 0))
    }

    @Test
    fun addEditViewModel_onConfirmation_dateWithSeparatorWhiteSpace() {
        viewModel.updateNumInput(3)
        viewModel.onConfirmation(ImageResponse(3, "30 12 2002"))
        assertEquals(viewModel.uiState.value.expiration, LocalDateTime.of(2002, 12, 30, 0, 0))
    }

    @Test
    fun addEditViewModel_onConfirmation_dateWithoutSeparator() {
        viewModel.updateNumInput(3)
        viewModel.onConfirmation(ImageResponse(3, "30122002"))
        assertEquals(viewModel.uiState.value.expiration, LocalDateTime.of(2002, 12, 30, 0, 0))
    }

    @Test
    fun addEditViewModel_onConfirmation_dateWithFormatDDMMYYY() {
        viewModel.updateNumInput(3)
        viewModel.onConfirmation(ImageResponse(3, "30/12/2002"))
        assertEquals(viewModel.uiState.value.expiration, LocalDateTime.of(2002, 12, 30, 0, 0))
    }

    @Test
    fun addEditViewModel_onConfirmation_dateWithFormatDDMMYY() {
        viewModel.updateNumInput(3)
        viewModel.onConfirmation(ImageResponse(3, "30/12/02"))
        assertEquals(viewModel.uiState.value.expiration, LocalDateTime.of(2002, 12, 30, 0, 0))
    }

    @Test
    fun addEditViewModel_onConfirmation_dateWithFormatMMYYYY() {
        viewModel.updateNumInput(3)
        viewModel.onConfirmation(ImageResponse(3, "12/2002"))
        assertEquals(viewModel.uiState.value.expiration, LocalDateTime.of(2002, 12, 1, 0, 0))
    }

    @Test
    fun addEditViewModel_onConfirmation_dateWithFormatMMYY() {
        viewModel.updateNumInput(3)
        viewModel.onConfirmation(ImageResponse(3, "12/02"))
        assertEquals(viewModel.uiState.value.expiration, LocalDateTime.of(2002, 12, 1, 0, 0))
    }
}