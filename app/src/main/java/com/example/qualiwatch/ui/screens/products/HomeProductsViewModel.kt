package com.example.qualiwatch.ui.screens.products

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.qualiwatch.QualiwatchApplication
import com.example.qualiwatch.R
import com.example.qualiwatch.data.ProductsRepository
import com.example.qualiwatch.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

data class HomeProductsUiState(
    val screen: Int = 0,
    val products: List<Product> = listOf(),
    val filteredProducts: List<Product> = listOf(),
    val hasError: Boolean = false,
    val query: String = "",
    val active: Boolean = false,
    val userMessage: Int? = null,
)


class HomeProductsViewModel(private val productsRepository: ProductsRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow(HomeProductsUiState())
    val uiState: StateFlow<HomeProductsUiState> = _uiState.asStateFlow()

    init {
        getProducts()
    }

    fun getProducts() {
        viewModelScope.launch {
            updateScreen(0)
            try {
                val products = productsRepository.getProducts()
                updateProducts(products)
                updateScreen(1)
            } catch (e: IOException) {
                updateScreen(2)
            } catch (e: Exception) {
                Log.d("erro", "getProducts: $e")
                updateScreen(2)
            }
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            try {
                productsRepository.deleteProduct(product)
                getProducts()
            } catch (_: Exception) {
                updateMessage(R.string.delete_error)
            }
        }
    }

    fun syncProduct() {
        viewModelScope.launch {
            try {
                productsRepository.syncProduct()
                getProducts()
            } catch (_: Exception) {
                updateMessage(R.string.syncErrorProduct)
            }
        }
    }

    fun search(query: String) {
        if (query.isEmpty()) {
            updateFilteredProducts(uiState.value.products)
        } else {
            val newList = _uiState.value.products.filter { it.nome.contains(query) }
            updateFilteredProducts(newList)
        }

    }

    private fun updateScreen(id: Int) {
        _uiState.update {
            it.copy(screen = id)
        }
    }

    private fun updateProducts(products: List<Product>) {
        _uiState.update {
            it.copy(products = products)
        }
        search(uiState.value.query)
    }

    private fun updateFilteredProducts(products: List<Product>) {
        _uiState.update { it.copy(filteredProducts = products) }
    }


    fun updateActive(active: Boolean) {
        _uiState.update { it.copy(active = active) }
    }

    fun updateQuery(query: String) {
        _uiState.update { it.copy(query = query) }
        search(query)
    }

    fun updateMessage(message: Int?) {
        _uiState.update { it.copy(userMessage = message) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val productsRepository = QualiwatchApplication.appContainer.productsRepository
                HomeProductsViewModel(productsRepository)
            }
        }
    }
}

