package com.example.shared.network.domain.exceptions

import kotlinx.io.IOException

sealed class AppException(override val message: String): IOException(message)