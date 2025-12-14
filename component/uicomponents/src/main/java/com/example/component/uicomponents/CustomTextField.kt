package com.example.component.uicomponents

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import com.example.component.uicomponents.theme.LocalExtendedColors

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    label: String,
    modifier: Modifier = Modifier,
    errorText: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    errorTrailingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                label,
                color = MaterialTheme.colorScheme.primary
            )
        },
        textStyle = MaterialTheme.typography.bodyLarge,
        enabled = enabled,
        isError = errorText != null,
        supportingText = {
            if (errorText != null)
                Text(errorText, style = MaterialTheme.typography.bodySmall)
        },
        singleLine = true,
        trailingIcon = if (errorText == null) trailingIcon else errorTrailingIcon,
        visualTransformation = visualTransformation,
        colors = TextFieldDefaults.colors(
            cursorColor = MaterialTheme.colorScheme.primary,
            errorCursorColor = MaterialTheme.colorScheme.error,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            disabledTextColor = LocalExtendedColors.current.surface3,
            errorTextColor = MaterialTheme.colorScheme.error,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            errorContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContainerColor = LocalExtendedColors.current.surface1,
            focusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            errorTrailingIconColor = MaterialTheme.colorScheme.error,
            selectionColors = TextSelectionColors(
                handleColor = MaterialTheme.colorScheme.primary,
                backgroundColor = LocalExtendedColors.current.surface1,
            ),
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.primary,
            disabledLabelColor = LocalExtendedColors.current.surface3,
            errorLabelColor = MaterialTheme.colorScheme.error,
            focusedSupportingTextColor = MaterialTheme.colorScheme.error,
            unfocusedSupportingTextColor = MaterialTheme.colorScheme.error,
            errorSupportingTextColor = MaterialTheme.colorScheme.error,
        ),
        modifier = modifier
    )
}