package com.example.nanopost.presentation.profileScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nanopost.domain.usecase.GetUserImagesUseCase
import com.example.nanopost.domain.usecase.GetUserPostsUseCase
import com.example.nanopost.domain.usecase.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getUserImagesUseCase: GetUserImagesUseCase,
    private val getUserPostsUseCase: GetUserPostsUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow<ProfileScreenState>(ProfileScreenState.Loading)
    val screenState: StateFlow<ProfileScreenState> = _screenState.asStateFlow()

    init {
        getUserProfile()
    }

    fun getUserProfile() {
        _screenState.value = ProfileScreenState.Loading
        viewModelScope.launch {
            val profile = async { getUserProfileUseCase() }
            val images = async { getUserImagesUseCase() }
            val posts = async { getUserPostsUseCase() }
            val res1 = profile.await()
            val res2 = images.await()
            val res3 = posts.await()
            _screenState.value = ProfileScreenState.Content(res1, res2, res3)
        }
    }

}