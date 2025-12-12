package com.example.nanopost.presentation.editProfileScreen

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nanopost.domain.usecase.GetUserProfileUseCase
import com.example.nanopost.domain.usecase.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
) : ViewModel() {

    private val _screenState =
        MutableStateFlow(EditProfileScreenState(null, null, null, null, null))
    val screenState: StateFlow<EditProfileScreenState> = _screenState.asStateFlow()

    val coroutineExceptionHandler = CoroutineExceptionHandler { context, throwable ->
        _screenState.update { currentState ->
            currentState.copy(
                updateResult = UpdateResult.Failure
            )
        }
    }

    fun startUp() {
        viewModelScope.launch {
            _screenState.update { currentState ->
                currentState.copy(
                    displayName = null,
                    bio = null,
                    avatarUrl = null,
                    avatarUri = null,
                    updateResult = null
                )
            }
            getProfile()
        }
    }

    fun getProfile() {
        viewModelScope.launch {
            val res = getUserProfileUseCase(null)
            _screenState.update { currentState ->
                currentState.copy(
                    displayName = res.displayName,
                    bio = res.bio,
                    avatarUrl = res.avatarSmall
                )
            }
        }
    }

    fun updateProfile() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _screenState.update { currentState ->
                currentState.copy(
                    updateResult = UpdateResult.Loading
                )
            }
            updateProfileUseCase(
                _screenState.value.displayName,
                _screenState.value.bio,
                _screenState.value.avatarUri?.toUri()
            )
            _screenState.update { currentState ->
                currentState.copy(
                    updateResult = UpdateResult.Success
                )
            }
        }
    }

    fun onDisplayNameChange(value: String) {
        _screenState.update { currentState ->
            currentState.copy(
                displayName = value
            )
        }
    }

    fun onBioChange(value: String) {
        _screenState.update { currentState ->
            currentState.copy(
                bio = value
            )
        }
    }

    fun onAvatarChange(value: String?) {
        _screenState.update { currentState ->
            currentState.copy(
                avatarUri = value
            )
        }
    }

    fun onClose() {
        _screenState.update { currentState ->
            currentState.copy(
                updateResult = null
            )
        }
    }
}