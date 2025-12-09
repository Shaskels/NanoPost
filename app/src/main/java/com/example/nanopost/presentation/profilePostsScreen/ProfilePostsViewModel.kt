package com.example.nanopost.presentation.profilePostsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.nanopost.domain.usecase.GetUserIdUseCase
import com.example.nanopost.domain.usecase.GetUserPostsUseCase
import com.example.nanopost.domain.usecase.LikePostUseCase
import com.example.nanopost.domain.usecase.UnlikePostUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ProfilePostsViewModel.ProfilePostsViewModelFactory::class)
class ProfilePostsViewModel @AssistedInject constructor(
    private val getUserPostsUseCase: GetUserPostsUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val likePostUseCase: LikePostUseCase,
    private val unlikePostUseCase: UnlikePostUseCase,
    @Assisted private val profileId: String?
) : ViewModel() {

    val posts = flow {
        val userId = profileId ?: getUserIdUseCase()
        emitAll(getUserPostsUseCase(userId))
    }.cachedIn(viewModelScope)

    fun likePost(postId: String) {
        viewModelScope.launch {
            likePostUseCase(postId)
        }
    }

    fun unlikePost(postId: String) {
        viewModelScope.launch {
            unlikePostUseCase(postId)
        }
    }

    @AssistedFactory
    interface ProfilePostsViewModelFactory {
        fun create(profileId: String?): ProfilePostsViewModel
    }
}