package com.example.nanopost.presentation.subscribersScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.component.uicomponents.CustomTopBar
import com.example.component.uicomponents.ErrorState
import com.example.component.uicomponents.Loading
import com.example.component.uicomponents.NoPhotoAvatar
import com.example.component.uicomponents.PhotoAvatar
import com.example.component.uicomponents.loadState
import com.example.nanopost.R
import com.example.shared.network.domain.exceptions.toAppException
import com.example.shared.domain.entity.ProfileCompact
import com.example.shared.network.domain.exceptions.AuthenticationException

@Composable
fun SubscribersScreen(
    subscribersViewModel: SubscribersViewModel,
    onSubscriberClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onLogout: () -> Unit,
) {
    val subscribers = subscribersViewModel.subscribers.collectAsLazyPagingItems()
    val pullToRefreshState = rememberPullToRefreshState()

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

        PullToRefreshBox(
            isRefreshing = subscribers.loadState.refresh is LoadState.Loading,
            onRefresh = subscribers::refresh,
            state = pullToRefreshState,
            modifier = Modifier.fillMaxSize()
        ) {

            when (val state = subscribers.loadState.refresh) {
                is LoadState.Error -> {
                    if (state.error.toAppException() is AuthenticationException) {
                        onLogout()
                    } else {
                        ErrorState(subscribers::retry)
                    }
                }

                is LoadState.Loading -> {
                    Loading()
                }

                else -> LazyColumn(
                    contentPadding = PaddingValues(
                        bottom = paddingValues.calculateBottomPadding() + 16.dp,
                        top = paddingValues.calculateTopPadding() + 16.dp,
                        start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                        end = paddingValues.calculateEndPadding(LayoutDirection.Ltr)
                    ),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxSize()
                ) {
                    items(
                        count = subscribers.itemCount,
                        key = subscribers.itemKey { it.id }
                    ) { index ->
                        val item = subscribers[index]
                        if (item != null) {
                            SubscriberItem(item, onSubscriberClick)
                        }
                    }

                    loadState(subscribers.loadState.append, onRetryClick = subscribers::retry)
                }
            }
        }
    }
}

@Composable
fun SubscriberItem(profile: ProfileCompact, onSubscriberClick: (String) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable(onClick = { onSubscriberClick(profile.id) })
    ) {
        if (profile.avatarUrl != null) {
            PhotoAvatar(profile.avatarUrl!!, modifier = Modifier.size(40.dp))
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