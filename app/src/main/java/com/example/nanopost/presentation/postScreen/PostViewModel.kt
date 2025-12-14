package com.example.nanopost.presentation.postScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nanopost.domain.usecase.DeletePostUseCase
import com.example.nanopost.domain.usecase.GetPostUseCase
import com.example.shared.settings.domain.usecase.GetUserIdUseCase
import com.example.shared.network.domain.exceptions.toAppException
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = PostViewModel.PostViewModelFactory::class)
class PostViewModel @AssistedInject constructor(
    private val getPostUseCase: GetPostUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    @Assisted private val postId: String,
): ViewModel() {

    private val _screenState = MutableStateFlow<PostScreenState>(PostScreenState.Loading)
    val screenState: StateFlow<PostScreenState> = _screenState.asStateFlow()

    val coroutineExceptionHandler = CoroutineExceptionHandler { context, throwable ->
        val appException = throwable.toAppException()
        _screenState.value = PostScreenState.Error(appException)
    }

    fun getPost() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _screenState.value = PostScreenState.Loading
            val res = getPostUseCase(postId)
            _screenState.value = PostScreenState.Content(res)
        }
    }

    suspend fun getUserId(): String {
        return getUserIdUseCase()
    }

    fun deletePost() {
        viewModelScope.launch(coroutineExceptionHandler) {
            deletePostUseCase(postId)
        }
    }

    @AssistedFactory
    interface PostViewModelFactory {
        fun create(postId: String): PostViewModel
    }
}