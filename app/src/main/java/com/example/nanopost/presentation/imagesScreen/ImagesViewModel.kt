package com.example.nanopost.presentation.imagesScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.nanopost.domain.usecase.GetProfileImagesUseCase
import com.example.shared.settings.domain.usecase.GetUserIdUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

@HiltViewModel(assistedFactory = ImagesViewModel.ImagesViewModelFactory::class)
class ImagesViewModel @AssistedInject constructor(
    private val getProfileImagesUseCase: GetProfileImagesUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    @Assisted profileId: String?,
): ViewModel() {

    val images = flow {
        val userId = profileId ?: getUserIdUseCase()
        emitAll(getProfileImagesUseCase(userId))
    }.cachedIn(viewModelScope)

    @AssistedFactory
    interface ImagesViewModelFactory {
        fun create(profileId: String?): ImagesViewModel
    }
}