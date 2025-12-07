package com.example.nanopost.presentation.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NoPhotoAvatar(name: String, modifier: Modifier = Modifier) {
    Card(
        shape = CircleShape,
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            name.firstOrNull().toString(),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(5.dp).align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}