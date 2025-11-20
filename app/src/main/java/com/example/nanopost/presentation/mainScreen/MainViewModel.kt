package com.example.nanopost.presentation.mainScreen

import androidx.lifecycle.ViewModel
import com.example.nanopost.domain.usecase.CheckUserLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val checkUserLoginUseCase: CheckUserLoginUseCase
): ViewModel() {

    suspend fun checkIfUserLogged(): Boolean{
        return checkUserLoginUseCase()
    }
}