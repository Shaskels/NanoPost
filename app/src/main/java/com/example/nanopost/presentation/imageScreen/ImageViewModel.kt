package com.example.nanopost.presentation.imageScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nanopost.domain.usecase.DeleteImageUseCase
import com.example.nanopost.domain.usecase.GetImageUseCase
import com.example.nanopost.domain.usecase.GetUserIdUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ImageViewModel.ImageViewModelFactory::class)
class ImageViewModel @AssistedInject constructor(
    private val deleteImageUseCase: DeleteImageUseCase,
    private val getImageUseCase: GetImageUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    @Assisted private val imageId: String,
): ViewModel() {

    private val _screenState = MutableStateFlow<ImageScreenState>(ImageScreenState.Loading)
    val screenState: StateFlow<ImageScreenState> = _screenState.asStateFlow()

    init {
        getImage()
    }

    fun getImage() {
        viewModelScope.launch {
            _screenState.value = ImageScreenState.Loading
            val res = getImageUseCase(imageId)
            _screenState.value = ImageScreenState.Content(res)
        }
    }

    fun deleteImage() {
        viewModelScope.launch {
            deleteImageUseCase(imageId)
        }
    }

    suspend fun getUserId(): String {
        return getUserIdUseCase()
    }

    @AssistedFactory
    interface ImageViewModelFactory {
        fun create(imageId: String): ImageViewModel
    }
}