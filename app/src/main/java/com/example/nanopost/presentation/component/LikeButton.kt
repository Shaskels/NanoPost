package com.example.nanopost.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.nanopost.R

@Composable
fun LikeButton(
    onClick: () -> Unit,
    likesCount: Int,
    liked: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(100.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = ButtonColors(
            contentColor = MaterialTheme.colorScheme.outline,
            containerColor = MaterialTheme.colorScheme.surface,
            disabledContentColor = MaterialTheme.colorScheme.outline,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
        ),
        modifier = modifier,
    ) {
        if (liked)
            Icon(
                painterResource(R.drawable.favorite_filled_yes),
                tint = MaterialTheme.colorScheme.error,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        else
            Icon(
                painterResource(R.drawable.favorite_filled_no),
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        Text(
            likesCount.toString(),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}