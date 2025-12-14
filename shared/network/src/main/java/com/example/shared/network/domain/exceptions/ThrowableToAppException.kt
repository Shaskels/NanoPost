package com.example.shared.network.domain.exceptions

fun Throwable.toAppException(): AppException {
    return when(this) {
        is WrongPasswordException -> WrongPasswordException(this.message)
        is InternetProblemException -> InternetProblemException(this.message)
        is AuthenticationException -> AuthenticationException(this.message)
        else -> UnknownException(this.message ?: "")
    }
}