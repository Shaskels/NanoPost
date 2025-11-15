package com.example.nanopost.presentation.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.nanopost.R
import com.example.nanopost.presentation.theme.LocalExtendedColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDialog(onDismissRequest: ()-> Unit, onConfirmButton: () -> Unit, title: String, text: String) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
            )
        },
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        confirmButton = {
            DarkButton(
                onClick = onConfirmButton,
                text = stringResource(R.string.confirm),
            )
        },
        dismissButton = {
            DarkButton(
                onClick = onDismissRequest,
                text = stringResource(R.string.cancel)
            )
        },
        containerColor = LocalExtendedColors.current.surface3,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}