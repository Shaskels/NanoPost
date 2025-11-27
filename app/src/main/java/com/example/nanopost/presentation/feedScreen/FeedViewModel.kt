package com.example.nanopost.presentation.feedScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nanopost.domain.usecase.GetPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow<FeedScreenState>(FeedScreenState.Initial)
    val screenState: StateFlow<FeedScreenState> = _screenState.asStateFlow()

    init {
        getPosts()
    }

    fun getPosts() {
        _screenState.value = FeedScreenState.Loading
        viewModelScope.launch {
            val res = getPostsUseCase()
            _screenState.value = FeedScreenState.Content(res)
        }
    }
}