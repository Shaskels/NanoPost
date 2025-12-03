package com.example.nanopost.presentation.feedScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nanopost.domain.usecase.GetPostsUseCase
import com.example.nanopost.domain.usecase.LikePostUseCase
import com.example.nanopost.domain.usecase.UnlikePostUseCase
import com.example.nanopost.presentation.authScreen.authScreenState.AuthScreenState
import com.example.nanopost.presentation.authScreen.toAppException
import com.example.nanopost.presentation.authScreen.toUiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase,
    private val likePostUseCase: LikePostUseCase,
    private val unlikePostUseCase: UnlikePostUseCase,
) : ViewModel() {

    private val _screenState = MutableStateFlow<FeedScreenState>(FeedScreenState.Initial)
    val screenState: StateFlow<FeedScreenState> = _screenState.asStateFlow()

    val coroutineExceptionHandler = CoroutineExceptionHandler { context, throwable ->
        _screenState.value = FeedScreenState.Error
    }

    init {
        getPosts()
    }

    fun getPosts() {
        _screenState.value = FeedScreenState.Loading
        viewModelScope.launch(coroutineExceptionHandler) {
            val res = getPostsUseCase()
            _screenState.value = FeedScreenState.Content(res)
        }
    }

    fun likePost(postId: String){
        viewModelScope.launch {
            likePostUseCase(postId)
        }
    }

    fun unlikePost(postId: String){
        viewModelScope.launch {
            unlikePostUseCase(postId)
        }
    }
}