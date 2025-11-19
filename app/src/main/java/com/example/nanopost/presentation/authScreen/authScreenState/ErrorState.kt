package com.example.nanopost.presentation.authScreen.authScreenState

sealed interface ErrorState {
    data object NoError : ErrorState
    data object InternetError : ErrorState
    data object WrongPasswordError: ErrorState
    data object UnknownError: ErrorState
}