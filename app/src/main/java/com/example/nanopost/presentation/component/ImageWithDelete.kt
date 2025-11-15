package com.example.nanopost.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.nanopost.R

@Composable
fun ImageWithDelete(onDeleteClick: () -> Unit, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(R.drawable.no_photo),
            modifier = Modifier
                .padding(5.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentDescription = null
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
                .clip(CircleShape)
        ) {
            Icon(
                painterResource(R.drawable.cancel),
                contentDescription = null,
            )
        }
    }
}