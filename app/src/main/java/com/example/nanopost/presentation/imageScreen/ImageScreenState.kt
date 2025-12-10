package com.example.nanopost.presentation.imageScreen

import com.example.nanopost.domain.entity.Image

sealed interface ImageScreenState {
    data object Error: ImageScreenState
    data object Loading: ImageScreenState
    data class Content(val image: Image): ImageScreenState
}