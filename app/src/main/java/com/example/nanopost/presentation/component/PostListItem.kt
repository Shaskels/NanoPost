package com.example.nanopost.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.component.uicomponents.CustomDivider
import com.example.component.uicomponents.LikeButton
import com.example.component.uicomponents.PhotoPager
import com.example.component.uicomponents.UiImage
import com.example.component.uicomponents.theme.LocalExtendedColors
import com.example.nanopost.domain.entity.Image
import com.example.nanopost.domain.entity.Post

@Composable
fun PostListItem(
    post: Post,
    onClick: (String) -> Unit,
    onImageClick: (String) -> Unit,
    onProfileClick: (String) -> Unit,
    onLikeClick: (String) -> Unit,
    onUnlikeClick: (String) -> Unit,
    isLiked: Boolean = false,
    isUnliked: Boolean = false,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardColors(
            containerColor = LocalExtendedColors.current.surface1,
            contentColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContainerColor = LocalExtendedColors.current.surface1,
            disabledContentColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier.clickable(onClick = {
            onClick(post.id)
        })
    )
    {
        UserPostInfo(
            owner = post.owner,
            dateCreated = post.dateCreated,
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 16.dp)
                .clickable(
                    onClick = {
                        onProfileClick(post.owner.id)
                    }
                ))

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
                post.images.map { it.toUiImage() },
                onImageClick,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        LikeButton(
            onClick = {
                if ((post.likes.liked  && !isUnliked) || isLiked)
                    onUnlikeClick(post.id)
                else
                    onLikeClick(post.id)
            },
            likesCount = if (isLiked) post.likes.likesCount + 1
            else post.likes.likesCount,
            liked = isLiked || (post.likes.liked  && !isUnliked),
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
        )

    }
}
fun Image.toUiImage() = UiImage(
    id = this.id,
    url = this.sizes.first().url,
    height = this.sizes.first().height,
    width = this.sizes.first().width,
)