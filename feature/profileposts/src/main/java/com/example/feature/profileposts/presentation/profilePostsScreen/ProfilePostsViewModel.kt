package com.example.feature.profileposts.presentation.profilePostsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.shared.domain.usecase.GetUserPostsUseCase
import com.example.shared.domain.usecase.LikePostUseCase
import com.example.shared.domain.usecase.UnlikePostUseCase
import com.example.shared.settings.domain.usecase.GetUserIdUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ProfilePostsViewModel.ProfilePostsViewModelFactory::class)
class ProfilePostsViewModel @AssistedInject constructor(
    private val getUserPostsUseCase: GetUserPostsUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val likePostUseCase: LikePostUseCase,
    private val unlikePostUseCase: UnlikePostUseCase,
    @Assisted private val profileId: String?
) : ViewModel() {

    private val _screenState = MutableStateFlow(LikesState(emptyList(), emptyList()))
    val screenState: StateFlow<LikesState> = _screenState.asStateFlow()

    val posts = flow {
        val userId = profileId ?: getUserIdUseCase()
        emitAll(getUserPostsUseCase(userId))
    }.cachedIn(viewModelScope)

    fun likePost(postId: String) {
        viewModelScope.launch {
            likePostUseCase(postId)
            val likedPosts = _screenState.value.likedPosts + postId
            val unlikedPosts = _screenState.value.unlikedPosts - postId
            _screenState.update { currentState ->
                currentState.copy(likedPosts = likedPosts, unlikedPosts = unlikedPosts)
            }
        }
    }

    fun unlikePost(postId: String) {
        viewModelScope.launch {
            unlikePostUseCase(postId)
            val likedPosts = _screenState.value.likedPosts - postId
            val unlikedPosts = _screenState.value.unlikedPosts + postId
            _screenState.update { currentState ->
                currentState.copy(likedPosts = likedPosts, unlikedPosts = unlikedPosts)
            }
        }
    }

    @AssistedFactory
    interface ProfilePostsViewModelFactory {
        fun create(profileId: String?): ProfilePostsViewModel
    }
}