package com.example.nanopost.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.component.uicomponents.NoPhotoAvatar
import com.example.component.uicomponents.PhotoAvatar
import com.example.shared.domain.entity.ProfileCompact
import com.example.util.datetime.dateTimeFormatter

@Composable
fun UserPostInfo(owner: ProfileCompact, dateCreated: Long, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier,
    ) {

        if (owner.avatarUrl == null) {
            NoPhotoAvatar(owner.displayName ?: owner.username, modifier = Modifier.size(40.dp))
        } else {
            PhotoAvatar(owner.avatarUrl!!, modifier = Modifier.size(40.dp))
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                owner.displayName ?: owner.username,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                dateTimeFormatter(dateCreated),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}