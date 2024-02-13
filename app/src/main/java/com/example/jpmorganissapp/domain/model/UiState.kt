package com.example.jpmorganissapp.domain.model

sealed class UiState<out R> {
    object Loading : UiState<Nothing>()
    class Success<out T>(var data: Any) : UiState<T>()
    class Error(var failure: String) : UiState<Nothing>()

    fun isError() = this is Error

    fun isSuccess() = this is Success
}