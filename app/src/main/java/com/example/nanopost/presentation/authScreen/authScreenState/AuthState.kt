package com.example.nanopost.presentation.authScreen.authScreenState

import com.example.nanopost.domain.entity.PasswordCheckResult
import com.example.nanopost.domain.entity.UsernameCheckResult

sealed interface AuthState {
    data object Login : AuthState

    data class Register(
        val usernameCheckResult: UsernameCheckResult,
        val passwordCheckResult: PasswordCheckResult
    ) : AuthState

    data class CheckName(val usernameCheckResult: UsernameCheckResult) : AuthState

    data object Logged: AuthState
}