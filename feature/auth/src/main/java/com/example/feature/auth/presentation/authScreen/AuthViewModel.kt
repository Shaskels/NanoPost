package com.example.feature.auth.presentation.authScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature.auth.domain.CheckUsernameUseCase
import com.example.feature.auth.domain.LoginUserUseCase
import com.example.feature.auth.domain.RegisterUserUseCase
import com.example.feature.auth.domain.validation.PasswordValidator
import com.example.feature.auth.presentation.authScreen.authScreenState.AuthScreenState
import com.example.feature.auth.presentation.authScreen.authScreenState.AuthState
import com.example.feature.auth.presentation.authScreen.authScreenState.ErrorState
import com.example.shared.domain.entity.PasswordCheckResult
import com.example.shared.domain.entity.UsernameCheckResult
import com.example.shared.network.domain.exceptions.AppException
import com.example.shared.network.domain.exceptions.AuthenticationException
import com.example.shared.network.domain.exceptions.InternetProblemException
import com.example.shared.network.domain.exceptions.UnknownException
import com.example.shared.network.domain.exceptions.WrongPasswordException
import com.example.shared.network.domain.exceptions.toAppException
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
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

    private val coroutineExceptionHandler = CoroutineExceptionHandler { context, throwable ->
        val appException = throwable.toAppException()
        _screenState.updateState<AuthScreenState.Content> { currentState ->
            currentState.copy(
                errorState = appException.toUiError()
            )
        }
    }
    fun checkUsername(username: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
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
        viewModelScope.launch(coroutineExceptionHandler) {
            loginUserUseCase(username, password)
            _screenState.updateState<AuthScreenState.Content> { currentState ->
                currentState.copy(
                    authState = AuthState.Logged
                )
            }
        }
    }

    fun registerUser(username: String, password: String, confirmPassword: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
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

    private fun AppException.toUiError(): ErrorState {
        return when (this) {
            is InternetProblemException -> ErrorState.InternetError
            is WrongPasswordException -> ErrorState.WrongPasswordError
            is UnknownException -> ErrorState.UnknownError
            is AuthenticationException -> ErrorState.UnknownError
        }
    }

}

