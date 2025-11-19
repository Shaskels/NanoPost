package com.example.nanopost.domain.exceptions

import kotlinx.io.IOException

sealed class AppException(override val message: String): IOException(message)