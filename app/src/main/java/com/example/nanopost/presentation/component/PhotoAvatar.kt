package com.example.nanopost.presentation.component

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.nanopost.R

@Composable
fun PhotoAvatar(url: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = url,
        contentDescription = null,
        contentScale = ContentScale.FillHeight,
        placeholder = painterResource(R.drawable.no_photo),
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
    )
}