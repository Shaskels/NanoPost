package com.example.nanopost.presentation.imageScreen

import com.example.nanopost.domain.entity.Image
import com.example.nanopost.domain.exceptions.AppException

sealed interface ImageScreenState {
    data class Error(val e: AppException): ImageScreenState
    data object Loading: ImageScreenState
    data class Content(val image: Image): ImageScreenState
}