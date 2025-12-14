package com.example.component.uicomponents

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.compositionLocalOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val LocalSnackbarHost = compositionLocalOf<CustomSnackbarHost> {
    error("No Snackbar Host State")
}
data class CustomSnackbarHost(val scope: CoroutineScope, val snackbarHostState: SnackbarHostState)

fun CustomSnackbarHost.showSnackbar(
    message: String,
    actionLabel: String?,
    onActionPerformed: () -> Unit,
    onDismiss: () -> Unit
) {
    scope.launch {
        val result = snackbarHostState
            .showSnackbar(
                message = message,
                actionLabel = actionLabel,
                duration = SnackbarDuration.Short
            )
        when (result) {
            SnackbarResult.ActionPerformed -> onActionPerformed()
            SnackbarResult.Dismissed -> {
                onDismiss()
            }
        }
    }
}