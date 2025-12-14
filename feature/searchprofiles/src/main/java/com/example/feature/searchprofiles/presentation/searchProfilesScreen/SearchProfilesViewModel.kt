package com.example.feature.searchprofiles.presentation.searchProfilesScreen

import androidx.lifecycle.ViewModel
import com.example.feature.searchprofiles.entity.SearchProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class SearchProfilesViewModel @Inject constructor(
    private val searchProfileUseCase: SearchProfileUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val profiles = query.debounce(600).flatMapLatest { query -> searchProfileUseCase(query) }


    fun onQueryChange(value: String) {
        _query.value = value
    }
}