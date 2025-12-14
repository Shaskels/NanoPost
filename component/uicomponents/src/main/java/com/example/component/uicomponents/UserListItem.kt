package com.example.component.uicomponents

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun UserListItem(userName: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    ListItem(
        headlineContent = {
            Text(userName, style = MaterialTheme.typography.bodyLarge)
        },
        leadingContent = {
            NoPhotoAvatar(userName)
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background,
            headlineColor = MaterialTheme.colorScheme.onSurface,
        ),
        modifier = modifier.clickable(onClick = onClick)
    )
}