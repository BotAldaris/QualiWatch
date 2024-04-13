package com.example.qualiwatch.ui.screens.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.qualiwatch.data.ProductsRepository
import com.example.qualiwatch.model.ImageResponse
import com.example.qualiwatch.model.Product
import com.example.qualiwatch.model.ProductPost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.LocalDateTime

data class AddEditProductScreenUiState(
    val id: String = "",
    val name: String = "",
    val batch: String = "",
    val expiration: LocalDateTime = LocalDate.now().atStartOfDay(),
    val imageResponseList: List<ImageResponse> = listOf(),
    val numInput: Int = 0,
    val showDialog: Boolean = false,
    val hasError: Boolean = false,
    val hasSaved: Boolean = false,
    val hasErrorInName: Boolean = true,
    val hasErrorInBatch: Boolean = true,
)

class AddEditProductViewModel(
    private val productsRepository: ProductsRepository,
    private val goToCamerax: (Int) -> Unit,
    product: Product?
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddEditProductScreenUiState())
    val uiState: StateFlow<AddEditProductScreenUiState> = _uiState.asStateFlow()

    init {
        if (product != null) {
            _uiState.update {
                it.copy(
                    id = product.id,
                    name = product.nome,
                    batch = product.lote,
                    expiration = product.validade,
                    hasErrorInBatch = false,
                    hasErrorInName = false
                )
            }
        }
    }

    fun onConfirmation(result: String?, reset: (String?) -> Unit) {
        if (result == null) return
        when (uiState.value.numInput) {
            3 -> {
                updateDateFromString(result)
            }

            1 -> {
                updateName(result)
            }

            2 -> {
                updateBatch(result)
            }
        }
        reset(null)
    }

    fun updateName(newName: String) {
        validateName(newName)
        _uiState.update {
            it.copy(name = newName)
        }
    }

    fun updateBatch(newBatch: String) {
        validateBatch(newBatch)
        _uiState.update {
            it.copy(batch = newBatch)
        }
    }

    fun updateExpiration(newExpiration: LocalDateTime?) {
        if (newExpiration != null) {
            _uiState.update {
                it.copy(expiration = newExpiration)
            }
        }
    }

    fun updateHasError(hasError: Boolean) {
        _uiState.update { it.copy(hasError = hasError) }
    }

    fun updateNumInput(num: Int) {
        _uiState.update { it.copy(numInput = num) }
    }

    fun saveProduct() {
        if (uiState.value.hasErrorInBatch || uiState.value.hasErrorInName) {
            updateHasError(true)
        } else {
            if (uiState.value.id == "") {
                createNewProduct()
            } else {
                updateProduct()
            }
        }
    }

    fun showCamera(num: Int) {
        goToCamerax(num)
        _uiState.update { it.copy(numInput = num) }
    }

    private fun validateName(newName: String) {
        if (newName.isBlank()) {
            _uiState.update { it.copy(hasErrorInName = true) }
        } else {
            if (uiState.value.hasErrorInName) {
                _uiState.update { it.copy(hasErrorInName = false) }
            }
        }
    }

    private fun validateBatch(newBatch: String) {
        if (newBatch.isBlank()) {
            _uiState.update { it.copy(hasErrorInBatch = true) }
        } else {
            if (uiState.value.hasErrorInBatch) {
                _uiState.update { it.copy(hasErrorInBatch = false) }
            }
        }
    }

    private fun createProductPost(): ProductPost {
        return ProductPost(uiState.value.name, uiState.value.batch, uiState.value.expiration)
    }

    private fun createNewProduct() = viewModelScope.launch {
        try {
            productsRepository.postProduct(createProductPost())
            _uiState.update { it.copy(hasSaved = true) }
        } catch (e: Exception) {
            updateHasError(true)
        }

    }

    private fun updateProduct() = viewModelScope.launch {
        try {
            productsRepository.putProducts(uiState.value.id, createProductPost())
            _uiState.update { it.copy(hasSaved = true) }
        } catch (e: Exception) {
            updateHasError(true)
        }
    }

    private fun updateDateFromString(dateString: String) {
        try {
            var ano = 2000
            var mes = 1
            var dia = 1
            val onlyNumber = dateString.replace(Regex("\\D"), "")
            if (onlyNumber.length == 8) {
                ano = onlyNumber.substring(4, 8).toInt()
                mes = onlyNumber.substring(2, 4).toInt()
                dia = onlyNumber.substring(0, 2).toInt()
            } else if (
                onlyNumber.length == 6 &&
                onlyNumber.substring(2, 6) < "2000"
            ) {
                ano = 2000 + onlyNumber.substring(4, 6).toInt()
                mes = onlyNumber.substring(2, 4).toInt()
                dia = onlyNumber.substring(0, 2).toInt()
            } else if (onlyNumber.length == 4) {
                ano = 2000 + onlyNumber.substring(2, 4).toInt()
                mes = onlyNumber.substring(0, 2).toInt()
            } else if (onlyNumber.length == 6) {
                ano = onlyNumber.substring(2, 6).toInt()
                mes = onlyNumber.substring(0, 2).toInt()
            } else {
                updateHasError(true)
            }
            val dateNew = LocalDateTime.of(ano, mes, dia, 0, 0)
            _uiState.update { it.copy(expiration = dateNew) }
        } catch (e: Exception) {
            updateHasError(true)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class AddEditProductViewModelFactory(
    private val repository: ProductsRepository,
    private val goToCamerax: (Int) -> Unit,
    private val productString: String?
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEditProductViewModel::class.java)) {
            var product: Product? = null
            if (productString != null) {
                product = Json.decodeFromString<Product>(productString)
            }
            return AddEditProductViewModel(
                repository,
                goToCamerax,
                product
            ) as T
        }
        throw IllegalArgumentException("ViewModel class not found.")
    }
}