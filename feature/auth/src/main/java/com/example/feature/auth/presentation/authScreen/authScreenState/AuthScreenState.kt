package com.example.feature.auth.presentation.authScreen.authScreenState

sealed interface AuthScreenState {
    data class Content(
        val authState: AuthState,
        val errorState: ErrorState,
    ): AuthScreenState
}

