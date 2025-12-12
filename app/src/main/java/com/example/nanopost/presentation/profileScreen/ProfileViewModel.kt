package com.example.nanopost.presentation.profileScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.nanopost.domain.exceptions.AppException
import com.example.nanopost.domain.exceptions.AuthenticationException
import com.example.nanopost.domain.exceptions.InternetProblemException
import com.example.nanopost.domain.exceptions.UnknownException
import com.example.nanopost.domain.exceptions.WrongPasswordException
import com.example.nanopost.domain.usecase.GetUserIdUseCase
import com.example.nanopost.domain.usecase.GetUserImagesPreviewUseCase
import com.example.nanopost.domain.usecase.GetUserPostsUseCase
import com.example.nanopost.domain.usecase.GetUserProfileUseCase
import com.example.nanopost.domain.usecase.LikePostUseCase
import com.example.nanopost.domain.usecase.SubscribeUseCase
import com.example.nanopost.domain.usecase.UnlikePostUseCase
import com.example.nanopost.domain.usecase.UnsubscribeUseCase
import com.example.nanopost.presentation.extentions.toAppException
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ProfileViewModel.ProfileViewModelFactory::class)
class ProfileViewModel @AssistedInject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getUserImagesUseCase: GetUserImagesPreviewUseCase,
    private val getUserPostsUseCase: GetUserPostsUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val likePostUseCase: LikePostUseCase,
    private val unlikePostUseCase: UnlikePostUseCase,
    private val subscribeUseCase: SubscribeUseCase,
    private val unsubscribeUseCase: UnsubscribeUseCase,
    @Assisted val profileId: String?,
) : ViewModel() {

    private val _screenState = MutableStateFlow<ProfileScreenState>(ProfileScreenState.Loading)
    val screenState: StateFlow<ProfileScreenState> = _screenState.asStateFlow()

    val posts = flow {
        val userId = profileId ?: getUserIdUseCase()
        emitAll(getUserPostsUseCase(userId))
    }.cachedIn(viewModelScope)

    val coroutineExceptionHandler = CoroutineExceptionHandler { context, throwable ->
        val appException = throwable.toAppException()
        _screenState.value = ProfileScreenState.Error(appException.toUiError())
    }

    fun getUserProfile() {
        _screenState.value = ProfileScreenState.Loading
        viewModelScope.launch(coroutineExceptionHandler) {
            val profile = async { getUserProfileUseCase(profileId) }
            val images = async { getUserImagesUseCase(profileId) }
            val res1 = profile.await()
            val res2 = images.await()
            _screenState.value =
                ProfileScreenState.Content(res1, res2, emptyList(), emptyList(), null)
        }
    }

    fun likePost(postId: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            likePostUseCase(postId)
            _screenState.updateState<ProfileScreenState.Content> { currentState ->
                val likedPosts = currentState.likedPosts + postId
                val unlikedPosts = currentState.unlikedPosts - postId
                currentState.copy(likedPosts = likedPosts, unlikedPosts = unlikedPosts)
            }
        }
    }

    fun unlikePost(postId: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            unlikePostUseCase(postId)
            _screenState.updateState<ProfileScreenState.Content> { currentState ->
                val likedPosts = currentState.likedPosts - postId
                val unlikedPosts = currentState.unlikedPosts + postId
                currentState.copy(likedPosts = likedPosts, unlikedPosts = unlikedPosts)
            }
        }
    }

    fun subscribe(profileId: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            subscribeUseCase(profileId)
            _screenState.updateState<ProfileScreenState.Content> { currentState ->
                currentState.copy(subscribed = true)
            }
        }
    }

    fun unsubscribe(profileId: String) {
        viewModelScope.launch {
            unsubscribeUseCase(profileId)
            _screenState.updateState<ProfileScreenState.Content> { currentState ->
                currentState.copy(subscribed = false)
            }
        }
    }

    private inline fun <reified T : ProfileScreenState> MutableStateFlow<ProfileScreenState>.updateState(
        block: (T) -> T
    ) {
        if (this.value is T) {
            this.update {
                block(this.value as T)
            }
        }
    }

    fun AppException.toUiError(): ErrorType {
        return when (this) {
            is InternetProblemException -> ErrorType.NetworkError
            is UnknownException -> ErrorType.UnknownError
            is AuthenticationException -> ErrorType.AuthenticationError
            is WrongPasswordException -> ErrorType.UnknownError
        }
    }

    @AssistedFactory
    interface ProfileViewModelFactory {
        fun create(profileId: String?): ProfileViewModel
    }
}