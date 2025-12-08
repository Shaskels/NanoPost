package com.example.nanopost.presentation.profilePostsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.nanopost.domain.usecase.GetUserIdUseCase
import com.example.nanopost.domain.usecase.GetUserPostsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

@HiltViewModel
class ProfilePostsViewModel @AssistedInject constructor(
    private val getUserPostsUseCase: GetUserPostsUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    @Assisted private val profileId: String?
): ViewModel() {

    val posts = flow {
        val userId = profileId ?: getUserIdUseCase()
        emitAll(getUserPostsUseCase(userId))
    }.cachedIn(viewModelScope)

    @AssistedFactory
    interface ProfilePostsViewModelFactory {
        fun create(characterId: String?): ProfilePostsViewModel
    }
}