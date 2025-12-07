package com.example.nanopost.presentation.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nanopost.domain.usecase.CheckUserLoginUseCase
import com.example.nanopost.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val checkUserLoginUseCase: CheckUserLoginUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _isUserAuth = MutableStateFlow<Boolean?>(null)
    val isUserAuth: StateFlow<Boolean?> = _isUserAuth.asStateFlow()

    init {
        checkIfUserLogged()
    }

    fun checkIfUserLogged() {
        viewModelScope.launch {
            _isUserAuth.value = checkUserLoginUseCase()
        }
    }

    fun logout() {
        viewModelScope.launch {
            _isUserAuth.value = false
            logoutUseCase()
        }
    }
}