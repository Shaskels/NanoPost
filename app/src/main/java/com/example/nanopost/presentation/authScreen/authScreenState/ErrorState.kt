package com.example.nanopost.presentation.authScreen.authScreenState

sealed interface ErrorState {
    data object NoError : ErrorState
    data class Error(val message: String) : ErrorState
}