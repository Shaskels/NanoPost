package com.example.nanopost.presentation.authScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nanopost.domain.entity.PasswordCheckResult
import com.example.nanopost.domain.entity.UsernameCheckResult
import com.example.nanopost.domain.usecase.CheckUsernameUseCase
import com.example.nanopost.domain.usecase.LoginUserUseCase
import com.example.nanopost.domain.usecase.RegisterUserUseCase
import com.example.nanopost.domain.validation.PasswordValidator
import com.example.nanopost.presentation.authScreen.authScreenState.AuthScreenState
import com.example.nanopost.presentation.authScreen.authScreenState.AuthState
import com.example.nanopost.presentation.authScreen.authScreenState.ErrorState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val passwordValidator: PasswordValidator,
    private val registerUserUseCase: RegisterUserUseCase,
    private val loginUserUseCase: LoginUserUseCase,
    private val checkUsernameUseCase: CheckUsernameUseCase,
) : ViewModel() {

    private val _screenState = MutableStateFlow<AuthScreenState>(
        AuthScreenState.Content(
            AuthState.CheckName(UsernameCheckResult.Free),
            ErrorState.NoError,
        )
    )
    val screenState: StateFlow<AuthScreenState> = _screenState.asStateFlow()

    fun checkUsername(username: String) {
        viewModelScope.launch {
            val res = checkUsernameUseCase(username)
            _screenState.updateState<AuthScreenState.Content> { currentState ->
                currentState.copy(
                    authState = when (res) {
                        UsernameCheckResult.Taken -> AuthState.Login
                        UsernameCheckResult.Free -> AuthState.Register(res, PasswordCheckResult.Ok)
                        else -> AuthState.CheckName(res)
                    }
                )
            }
        }
    }

    fun loginUser(username: String, password: String) {
        viewModelScope.launch {
            loginUserUseCase(username, password)
            _screenState.updateState<AuthScreenState.Content> { currentState ->
                currentState.copy(
                    authState = AuthState.Logged
                )
            }
        }
    }

    fun registerUser(username: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            val res = passwordValidator.validatePassword(password, confirmPassword)
            _screenState.updateState<AuthScreenState.Content> { currentState ->
                currentState.copy(
                    authState = AuthState.Register(UsernameCheckResult.Free, res)
                )
            }
            if (res == PasswordCheckResult.Ok) {
                registerUserUseCase(username, password)
                _screenState.updateState<AuthScreenState.Content> { currentState ->
                    currentState.copy(
                        authState = AuthState.Logged
                    )
                }
            }
        }
    }

    private inline fun <reified T : AuthScreenState> MutableStateFlow<AuthScreenState>.updateState(
        block: (T) -> T
    ) {
        if (this.value is T) {
            this.update {
                block(this.value as T)
            }
        }
    }

}