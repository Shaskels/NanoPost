package com.example.nanopost.presentation.profileScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.nanopost.domain.usecase.GetUserIdUseCase
import com.example.nanopost.domain.usecase.GetUserImagesUseCase
import com.example.nanopost.domain.usecase.GetUserPostsUseCase
import com.example.nanopost.domain.usecase.GetUserProfileUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @AssistedInject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getUserImagesUseCase: GetUserImagesUseCase,
    private val getUserPostsUseCase: GetUserPostsUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    @Assisted val profileId: String?,
) : ViewModel() {

    private val _screenState = MutableStateFlow<ProfileScreenState>(ProfileScreenState.Loading)
    val screenState: StateFlow<ProfileScreenState> = _screenState.asStateFlow()

    val posts = flow {
        val userId = profileId ?: getUserIdUseCase()
        emitAll(getUserPostsUseCase(userId))
    }.cachedIn(viewModelScope)

    init {
        getUserProfile()
    }

    fun getUserProfile() {
        _screenState.value = ProfileScreenState.Loading
        viewModelScope.launch {
            val profile = async { getUserProfileUseCase(profileId) }
            val images = async { getUserImagesUseCase(profileId) }
            val res1 = profile.await()
            val res2 = images.await()
            _screenState.value = ProfileScreenState.Content(res1, res2)
        }
    }

    @AssistedFactory
    interface ProfileViewModelFactory {
        fun create(characterId: String?): ProfileViewModel
    }
}