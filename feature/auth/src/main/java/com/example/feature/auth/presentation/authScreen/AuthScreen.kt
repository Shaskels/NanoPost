package com.example.feature.auth.presentation.authScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.component.uicomponents.CustomTextField
import com.example.component.uicomponents.LightButton
import com.example.component.uicomponents.LocalSnackbarHost
import com.example.component.uicomponents.showSnackbar
import com.example.feature.auth.R
import com.example.feature.auth.presentation.authScreen.authScreenState.AuthScreenState
import com.example.feature.auth.presentation.authScreen.authScreenState.AuthState
import com.example.feature.auth.presentation.authScreen.authScreenState.ErrorState
import com.example.shared.domain.entity.PasswordCheckResult
import com.example.shared.domain.entity.UsernameCheckResult
import com.example.component.uicomponent.R as uiComponentsR

@Composable
fun AuthScreen(onLogged: () -> Unit, authViewModel: AuthViewModel = hiltViewModel()) {

    val screenState = authViewModel.screenState.collectAsState()

    when (val currentState = screenState.value) {
        is AuthScreenState.Content -> Screen(onLogged, currentState, authViewModel)
    }

}

@Composable
fun Screen(
    onLogged: () -> Unit,
    screenState: AuthScreenState.Content,
    authViewModel: AuthViewModel
) {
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    val snackbarHost = LocalSnackbarHost.current
    val resources = LocalResources.current

    LaunchedEffect(screenState.errorState) {
        if (screenState.errorState != ErrorState.NoError) {
            snackbarHost.showSnackbar(
                message = when (screenState.errorState) {
                    ErrorState.InternetError -> resources.getString(R.string.network_connection_error)
                    ErrorState.UnknownError -> resources.getString(R.string.something_went_wrong)
                    ErrorState.WrongPasswordError -> resources.getString(R.string.wrong_password)
                    else -> ""
                },
                actionLabel = resources.getString(uiComponentsR.string.retry),
                onActionPerformed = {
                    when (screenState.authState) {
                        is AuthState.CheckName -> authViewModel.checkUsername(username)
                        AuthState.Login -> authViewModel.loginUser(username, password)
                        is AuthState.Register -> authViewModel.registerUser(
                            username,
                            password,
                            confirmPassword
                        )

                        AuthState.Logged -> {}
                    }
                },
                onDismiss = {}
            )
        }
    }

    LaunchedEffect(screenState.authState) {
        if (screenState.authState == AuthState.Logged) {
            onLogged()
        }
    }

    Scaffold(modifier = Modifier) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Text(
                stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            CustomTextField(
                value = username,
                onValueChange = { username = it },
                enabled = screenState.authState is AuthState.CheckName,
                label = stringResource(R.string.username),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                errorText = if (screenState.authState is AuthState.CheckName) {
                    getUsernameError(screenState.authState.usernameCheckResult)
                } else null,
                errorTrailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.error_24px),
                        contentDescription = null
                    )
                },
            )

            if (screenState.authState is AuthState.Login || screenState.authState is AuthState.Register) {
                CustomTextField(
                    value = password,
                    onValueChange = { password = it },
                    enabled = true,
                    errorText = if (screenState.authState is AuthState.Register) {
                        getPasswordError(screenState.authState.passwordCheckResult)
                    } else null,
                    label = stringResource(R.string.password),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    errorTrailingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.error_24px),
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        if (showPassword) {
                            IconButton(onClick = { showPassword = false }) {
                                Icon(
                                    painter = painterResource(R.drawable.visible_true),
                                    contentDescription = null
                                )
                            }
                        } else {
                            IconButton(onClick = { showPassword = true }) {
                                Icon(
                                    painter = painterResource(R.drawable.visible_false),
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation()
                )
            }

            if (screenState.authState is AuthState.Register) {
                CustomTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    enabled = true,
                    errorText = when (screenState.authState.passwordCheckResult) {
                        PasswordCheckResult.ConfirmFailed -> stringResource(R.string.password_not_match_error)
                        else -> null
                    },
                    label = stringResource(R.string.confirm_password),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    errorTrailingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.error_24px),
                            contentDescription = null
                        )
                    },
                )
            }

            LightButton(
                onClick = {
                    when (screenState.authState) {
                        is AuthState.CheckName -> {
                            authViewModel.checkUsername(username)
                        }

                        AuthState.Login -> {
                            authViewModel.loginUser(username, password)
                        }

                        is AuthState.Register -> {
                            authViewModel.registerUser(username, password, confirmPassword)
                        }

                        AuthState.Logged -> {}
                    }
                },
                text = when (screenState.authState) {
                    is AuthState.CheckName -> stringResource(R.string.continue_auth)
                    is AuthState.Login -> stringResource(R.string.sign_in)
                    is AuthState.Register -> stringResource(R.string.register)
                    AuthState.Logged -> ""
                },
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun getUsernameError(usernameCheckResult: UsernameCheckResult): String? {
    return when (usernameCheckResult) {
        UsernameCheckResult.TooShort -> stringResource(R.string.username_tooshort_error)
        UsernameCheckResult.TooLong -> stringResource(R.string.username_toolong_error)
        UsernameCheckResult.InvalidCharacter -> stringResource(R.string.username_invalidcharacters_error)
        else -> null
    }
}

@Composable
fun getPasswordError(passwordCheckResult: PasswordCheckResult): String? {
    return when (passwordCheckResult) {
        PasswordCheckResult.TooShort -> stringResource(R.string.password_tooshort_error)
        else -> null
    }
}