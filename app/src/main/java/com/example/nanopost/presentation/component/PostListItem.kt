package com.example.nanopost.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nanopost.domain.entity.Post
import com.example.nanopost.presentation.theme.LocalExtendedColors

@Composable
fun PostListItem(post: Post, onLikeClick: (String) -> Unit, onUnlikeClick: (String) -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardColors(
            containerColor = LocalExtendedColors.current.surface1,
            contentColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContainerColor = LocalExtendedColors.current.surface1,
            disabledContentColor = MaterialTheme.colorScheme.surfaceVariant
        )
    )
    {
        UserPostInfo(post = post, modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp))

        CustomDivider()

        if (!post.text.isNullOrEmpty()) {
            Text(
                post.text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
            )
        }

        if (post.images.isNotEmpty()) {
            PhotoPager(
                post.images,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        LikeButton(
            onClick = {
                if (post.likes.liked)
                    onUnlikeClick(post.id)
                else
                    onLikeClick(post.id)
            },
            likesCount = post.likes.likesCount,
            liked = post.likes.liked,
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
        )

    }
}