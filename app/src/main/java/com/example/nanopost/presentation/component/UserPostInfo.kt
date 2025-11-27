package com.example.nanopost.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nanopost.domain.entity.Post

@Composable
fun UserPostInfo(post: Post, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier,
    ) {
        NoPhotoAvatar(post.owner.username)

        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                post.owner.username,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                post.dataCreated.toString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}