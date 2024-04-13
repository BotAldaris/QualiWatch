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
import com.example.qualiwatch.util.Async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime

data class AlertScreenUiState(
    val products: List<Product> = listOf(Product("1", "b", "c", LocalDateTime.now())),
    val userMessage: Int? = null,
    val isLoading: Boolean = false
)

class AlertScreenViewModel(private val nearExpirationRepository: NearExpirationRepository) :
    ViewModel() {
    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _alertsAsync =
        nearExpirationRepository.getProductsFlow().map { Async.Success(it) }
            .catch<Async<List<Product>>> { emit(Async.Error(R.string.loading_alerts_error)) }
    private val _isLoading = MutableStateFlow(false)

    //    val alertScreenUiState: StateFlow<AlertScreenUiState> =
//        nearExpirationRepository.getProductsFlow().map { AlertScreenUiState(it) }
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(
//                    TIMEOUT_MILLIS
//                ), initialValue = AlertScreenUiState()
//            )
    val alertScreenUiState: StateFlow<AlertScreenUiState> =
        combine(_isLoading, _userMessage, _alertsAsync) { isLoading, userMessage, alertAsync ->
            when (alertAsync) {
                Async.Loading -> {
                    AlertScreenUiState(isLoading = true)
                }

                is Async.Error -> {
                    AlertScreenUiState(userMessage = alertAsync.errorMessage)
                }

                is Async.Success -> {
                    AlertScreenUiState(
                        products = alertAsync.data,
                        isLoading = isLoading,
                        userMessage = userMessage
                    )
                }
            }

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = AlertScreenUiState(isLoading = true)
        )

    fun syncProduct() {
        viewModelScope.launch {
            try {
                nearExpirationRepository.syncProduct()
            } catch (e: Exception) {
                _userMessage.value = R.string.syncError
            }
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            nearExpirationRepository.deleteProduct(product)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            nearExpirationRepository.deleteAll()
        }
    }

    fun snackbarMessageShown() {
        _userMessage.value = null
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