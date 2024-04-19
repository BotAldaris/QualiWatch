package com.example.qualiwatch.ui.screens.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.qualiwatch.QualiwatchApplication
import com.example.qualiwatch.R
import com.example.qualiwatch.data.NearExpirationRepository
import com.example.qualiwatch.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

data class AlertScreenUiState(
    val products: List<Product> = listOf(),
    val userMessage: Int? = null,
    val screen: Int = 0
)

class AlertScreenViewModel(private val nearExpirationRepository: NearExpirationRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow(AlertScreenUiState())
    val alertScreenUiState: StateFlow<AlertScreenUiState> = _uiState.asStateFlow()

    init {
        getAlerts()
    }

    fun syncProduct() {
        viewModelScope.launch {
            try {
                val products = nearExpirationRepository.getProducts()
                updateProducts(products)
                updateScreen(1)
            } catch (e: IOException) {
                updateMessage(R.string.syncError)
            } catch (e: HttpException) {
                updateMessage(R.string.syncError)
            } catch (e: Exception) {
                updateMessage(R.string.syncError)
            }
        }
    }

    fun getAlerts() {
        viewModelScope.launch {
            updateScreen(0)
            try {
                val products = nearExpirationRepository.getProducts()
                updateProducts(products)
                updateScreen(1)
            } catch (e: IOException) {
                updateScreen(2)
            } catch (e: HttpException) {
                updateScreen(2)
            }
        }
    }

    fun snackbarMessageShown() {
        updateMessage(null)
    }

    private fun updateScreen(i: Int) {
        _uiState.update { it.copy(screen = i) }
    }

    private fun updateMessage(message: Int?) {
        _uiState.update { it.copy(userMessage = message) }
    }

    private fun updateProducts(products: List<Product>) {
        _uiState.update { it.copy(products = products) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AlertScreenViewModel(QualiwatchApplication.appContainer.nearExpirationRepository)
            }
        }
        private const val TIMEOUT_MILLIS = 5_000L
    }
}