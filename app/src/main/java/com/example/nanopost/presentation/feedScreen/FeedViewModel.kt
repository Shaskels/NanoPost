package com.example.nanopost.presentation.feedScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.nanopost.domain.exceptions.AppException
import com.example.nanopost.domain.exceptions.AuthenticationException
import com.example.nanopost.domain.exceptions.InternetProblemException
import com.example.nanopost.domain.exceptions.UnknownException
import com.example.nanopost.domain.exceptions.WrongPasswordException
import com.example.nanopost.domain.usecase.GetFeedUseCase
import com.example.nanopost.domain.usecase.LikePostUseCase
import com.example.nanopost.domain.usecase.UnlikePostUseCase
import com.example.nanopost.presentation.extentions.toAppException
import com.example.nanopost.presentation.feedScreen.screenState.FeedScreenState
import com.example.nanopost.presentation.feedScreen.screenState.LikeErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    getFeedUseCase: GetFeedUseCase,
    private val likePostUseCase: LikePostUseCase,
    private val unlikePostUseCase: UnlikePostUseCase,
) : ViewModel() {

    private val _screenState = MutableStateFlow(
        FeedScreenState(
            emptyList(),
            emptyList(),
            LikeErrors.NoError,
        )
    )
    val screenState: StateFlow<FeedScreenState> = _screenState.asStateFlow()
    val posts = getFeedUseCase().cachedIn(viewModelScope)

    val coroutineExceptionHandler = CoroutineExceptionHandler { context, throwable ->
        val appException = throwable.toAppException()
        _screenState.update { currentState ->
            currentState.copy(likeError = appException.toUiError())
        }
    }

    fun likePost(postId: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            likePostUseCase(postId)
            _screenState.update { currentState ->
                val likedPosts = currentState.likedPosts + postId
                val unlikedPosts = currentState.unlikedPosts - postId
                currentState.copy(likedPosts = likedPosts, unlikedPosts = unlikedPosts)
            }
        }
    }

    fun unlikePost(postId: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            unlikePostUseCase(postId)
            _screenState.update { currentState ->
                val likedPosts = currentState.likedPosts - postId
                val unlikedPosts = currentState.unlikedPosts + postId
                currentState.copy(likedPosts = likedPosts, unlikedPosts = unlikedPosts)
            }
        }
    }

    fun AppException.toUiError(): LikeErrors {
        return when (this) {
            is InternetProblemException -> LikeErrors.NetworkError
            is UnknownException -> LikeErrors.UnknownError
            is AuthenticationException -> LikeErrors.AuthenticationError
            is WrongPasswordException -> LikeErrors.UnknownError
        }
    }
}