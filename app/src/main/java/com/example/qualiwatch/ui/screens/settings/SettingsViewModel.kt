package com.example.qualiwatch.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.qualiwatch.QualiwatchApplication
import com.example.qualiwatch.R
import com.example.qualiwatch.data.UserPreferencesRepository
import com.example.qualiwatch.data.source.local.UserPreferences
import com.example.qualiwatch.util.Async
import com.example.qualiwatch.util.WhileUiSubscribed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime

data class SettingsUiState(
    val userPreferences: UserPreferences = UserPreferences(1, true, LocalDateTime.now(), false),
    val userMessage: Int? = null,
    val isLoading: Boolean = false
)

class SettingsViewModel(private val repository: UserPreferencesRepository) : ViewModel() {

    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _userPreferencesAsync =
        repository.getUserPreferencesFlow().map { Async.Success(it) }
            .catch<Async<UserPreferences>> { emit(Async.Error(R.string.error_loading_settings)) }

    val uiState: StateFlow<SettingsUiState> = combine(
        _isLoading,
        _userMessage,
        _userPreferencesAsync
    ) { isLoading, userMessage, userPreferencesAsync ->
        when (userPreferencesAsync) {
            Async.Loading -> {
                SettingsUiState(isLoading = true)
            }

            is Async.Error -> {
                SettingsUiState(userMessage = userPreferencesAsync.errorMessage)
            }

            is Async.Success -> {
                SettingsUiState(
                    userPreferences = userPreferencesAsync.data,
                    userMessage = userMessage,
                    isLoading = isLoading
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = SettingsUiState(isLoading = true)
    )

    fun updateDark(dark: Boolean) {
        viewModelScope.launch {
            repository.updateDark(dark)
        }
    }

    fun updateSaveOnline(online: Boolean) {
        viewModelScope.launch {
            repository.updateSaveOnline(online)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(QualiwatchApplication.appContainer.userPreferencesRepository)
            }
        }
    }
}