package com.example.component.uicomponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.component.uicomponent.R


@Composable
fun AddImageButton(onClick: () -> Unit, label: String, modifier: Modifier = Modifier) {
    AssistChip(
        onClick = onClick,
        label = {
            Text(label, style = MaterialTheme.typography.labelLarge)
        },
        modifier = modifier,
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.add_photo_alternate),
                contentDescription = null
            )
        },
        colors = AssistChipDefaults.assistChipColors(
            leadingIconContentColor = MaterialTheme.colorScheme.primary,
            labelColor = MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    )
}