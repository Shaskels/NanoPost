package com.example.nanopost.presentation.component

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.toUri
import com.example.nanopost.R

@Composable
fun ImageWithDelete(uri: Uri, onDeleteClick: () -> Unit, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = uri,
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            placeholder = painterResource(R.drawable.no_photo),
            modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        IconButton(
            onClick = onDeleteClick, colors = IconButtonColors(
                containerColor = MaterialTheme.colorScheme.outlineVariant,
                contentColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContentColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContainerColor = MaterialTheme.colorScheme.outlineVariant
            ),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(25.dp)
                .clip(CircleShape)
        ) {
            Icon(
                painterResource(R.drawable.cancel),
                contentDescription = null,
            )
        }
    }
}