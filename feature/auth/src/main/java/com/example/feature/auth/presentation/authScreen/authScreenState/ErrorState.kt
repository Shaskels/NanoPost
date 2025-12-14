package com.example.feature.auth.presentation.authScreen.authScreenState

sealed interface ErrorState {
    data object NoError : ErrorState
    data object InternetError : ErrorState
    data object WrongPasswordError: ErrorState
    data object UnknownError: ErrorState
}