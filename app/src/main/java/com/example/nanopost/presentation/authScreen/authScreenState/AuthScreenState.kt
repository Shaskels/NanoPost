package com.example.nanopost.presentation.authScreen.authScreenState

sealed interface AuthScreenState {
    data class Content(
        val authState: AuthState,
        val errorState: ErrorState,
    ): AuthScreenState
}

