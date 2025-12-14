package com.example.component.uicomponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import com.example.component.uicomponent.R

fun LazyListScope.loadState(state: LoadState, onRetryClick: () -> Unit) {
    when (state) {
        is LoadState.Error -> item {
            ErrorState(onRetryClick)
        }

        LoadState.Loading -> item {
            LoadingState()
        }

        is LoadState.NotLoading -> Unit
    }
}

fun LazyGridScope.loadState(state: LoadState, onRetryClick: () -> Unit) {
    when (state) {
        is LoadState.Error -> item {
            ErrorState(onRetryClick)
        }

        LoadState.Loading -> item {
            LoadingState()
        }

        is LoadState.NotLoading -> Unit
    }
}

@Composable
fun LoadingState() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@Composable
fun ErrorState(onRetryClick: () -> Unit) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                stringResource(R.string.failed_to_load),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelLarge
            )

            LightButton(
                onClick = onRetryClick,
                text = stringResource(R.string.retry),
            )
        }
    }
}