package com.example.shared.network.domain.exceptions

data class WrongPasswordException(override val message: String): AppException(message)
data class InternetProblemException(override val message: String): AppException(message)
data class UnknownException(override val message: String): AppException(message)
data class AuthenticationException(override val message: String): AppException(message)