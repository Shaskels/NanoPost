package com.example.nanopost.presentation.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomSnackbar(
    onConfirmAction: () -> Unit,
    actionText: String,
    text: String,
    modifier: Modifier = Modifier
) {
    Snackbar(
        action = {
            LightButton(
                onClick = onConfirmAction,
                text = actionText,
            )
        },
        shape = RoundedCornerShape(4.dp),
        containerColor = MaterialTheme.colorScheme.inverseSurface,
        contentColor = MaterialTheme.colorScheme.inverseOnSurface,
        modifier = modifier
    ) {
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}