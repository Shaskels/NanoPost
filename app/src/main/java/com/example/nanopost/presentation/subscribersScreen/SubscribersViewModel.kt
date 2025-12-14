package com.example.nanopost.presentation.subscribersScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.nanopost.domain.usecase.GetProfileSubscribersUseCase
import com.example.shared.settings.domain.usecase.GetUserIdUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

@HiltViewModel(assistedFactory = SubscribersViewModel.SubscribersViewModelFactory::class)
class SubscribersViewModel @AssistedInject constructor(
    private val getProfileSubscribersUseCase: GetProfileSubscribersUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    @Assisted private val profileId: String?
): ViewModel() {

    val subscribers = flow {
        val userId = profileId ?: getUserIdUseCase()
        emitAll(getProfileSubscribersUseCase(userId))
    }.cachedIn(viewModelScope)

    @AssistedFactory
    interface SubscribersViewModelFactory {
        fun create(profileId: String?): SubscribersViewModel
    }
}