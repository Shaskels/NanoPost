package com.example.nanopost.presentation.subscribersScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.nanopost.R
import com.example.nanopost.domain.entity.ProfileCompact
import com.example.nanopost.presentation.component.CustomTopBar
import com.example.nanopost.presentation.component.NoPhotoAvatar
import com.example.nanopost.presentation.component.PhotoAvatar
import com.example.nanopost.presentation.component.loadState

@Composable
fun SubscribersScreen(subscribersViewModel: SubscribersViewModel, onBackClick: () -> Unit) {
    val subscribers = subscribersViewModel.subscribers.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            CustomTopBar(
                title = stringResource(R.string.subscribers_big),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = null
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            items(
                count = subscribers.itemCount,
                key = subscribers.itemKey { it.id }
            ) { index ->
                val item = subscribers[index]
                if (item != null) {
                    SubscriberItem(item)
                }
            }

            loadState(subscribers.loadState.append, onRetryClick = subscribers::retry)
        }
    }
}

@Composable
fun SubscriberItem(profile: ProfileCompact) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        if (profile.avatarUrl != null) {
            PhotoAvatar(profile.avatarUrl, modifier = Modifier.size(40.dp))
        } else {
            NoPhotoAvatar(profile.username, modifier = Modifier.size(40.dp))
        }

        Text(
            profile.username,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
        )
    }
}