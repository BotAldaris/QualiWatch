package com.example.qualiwatch.util

sealed class Async<out T> {
    data object Loading : Async<Nothing>()

    data class Error(val errorMessage: Int) : Async<Nothing>()

    data class Success<out T>(val data: T) : Async<T>()
}
