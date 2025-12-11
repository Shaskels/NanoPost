package com.example.nanopost.presentation.extentions

import com.example.nanopost.domain.exceptions.AppException
import com.example.nanopost.domain.exceptions.AuthenticationException
import com.example.nanopost.domain.exceptions.InternetProblemException
import com.example.nanopost.domain.exceptions.UnknownException
import com.example.nanopost.domain.exceptions.WrongPasswordException

fun Throwable.toAppException(): AppException {
    return when(this) {
        is WrongPasswordException -> WrongPasswordException(this.message)
        is InternetProblemException -> InternetProblemException(this.message)
        is AuthenticationException -> AuthenticationException(this.message)
        else -> UnknownException(this.message ?: "")
    }
}