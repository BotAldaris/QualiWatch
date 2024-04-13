package com.example.qualiwatch.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.qualiwatch.QualiwatchApplication
import com.example.qualiwatch.data.UserPreferencesRepository
import com.example.qualiwatch.util.WhileUiSubscribed
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ThemeViewModel(repository: UserPreferencesRepository) : ViewModel() {
    private val _darkAsync = repository.getDarkFlow().map { it }.catch { true }
    val dark = _darkAsync.stateIn(viewModelScope, WhileUiSubscribed, true)

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ThemeViewModel(QualiwatchApplication.appContainer.userPreferencesRepository)
            }
        }
    }

}