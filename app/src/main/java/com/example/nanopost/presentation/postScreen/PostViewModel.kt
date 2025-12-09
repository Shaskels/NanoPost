package com.example.nanopost.presentation.postScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nanopost.domain.usecase.DeletePostUseCase
import com.example.nanopost.domain.usecase.GetPostUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = PostViewModel.PostViewModelFactory::class)
class PostViewModel @AssistedInject constructor(
    private val getPostUseCase: GetPostUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    @Assisted private val postId: String,
): ViewModel() {

    private val _screenState = MutableStateFlow<PostScreenState>(PostScreenState.Loading)
    val screenState: StateFlow<PostScreenState> = _screenState.asStateFlow()

    init {
        getPost()
    }

    fun getPost() {
        viewModelScope.launch {
            _screenState.value = PostScreenState.Loading
            val res = getPostUseCase(postId)
            _screenState.value = PostScreenState.Content(res)
        }
    }

    fun deletePost() {
        viewModelScope.launch {
            _screenState.value = PostScreenState.Loading
            deletePostUseCase(postId)
        }
    }

    @AssistedFactory
    interface PostViewModelFactory {
        fun create(postId: String): PostViewModel
    }
}