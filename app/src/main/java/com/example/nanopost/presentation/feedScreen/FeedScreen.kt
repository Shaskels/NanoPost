package com.example.nanopost.presentation.feedScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.nanopost.R
import com.example.nanopost.domain.exceptions.AuthenticationException
import com.example.nanopost.presentation.component.CustomTopBar
import com.example.nanopost.presentation.component.FloatingButton
import com.example.nanopost.presentation.component.LightButton
import com.example.nanopost.presentation.component.Loading
import com.example.nanopost.presentation.component.PostListItem
import com.example.nanopost.presentation.component.loadState
import com.example.nanopost.presentation.extentions.toAppException
import com.example.nanopost.presentation.mainScreen.LocalSnackbarHost
import com.example.nanopost.presentation.mainScreen.showSnackbar

@Composable
fun FeedScreen(
    onImageClick: (String) -> Unit,
    onProfileClick: (String) -> Unit,
    onNewPostAdd: () -> Unit,
    onPostClick: (String) -> Unit,
    onLogout: () -> Unit,
    feedViewModel: FeedViewModel = hiltViewModel()
) {
    val screenState by feedViewModel.screenState.collectAsState()
    val snackbarHost = LocalSnackbarHost.current
    val feed = feedViewModel.posts.collectAsLazyPagingItems()
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(screenState.likeError) {
        if (screenState.likeError != LikeErrors.NoError) {
            snackbarHost.showSnackbar(
                message = when (screenState.likeError) {
                    LikeErrors.AuthenticationError -> {
                        onLogout()
                        "You're logged out"
                    }

                    LikeErrors.NetworkError -> "Something wrong with your network"
                    LikeErrors.NoError -> ""
                    LikeErrors.UnknownError -> "Failed to like"
                },
                actionLabel = null,
                onActionPerformed = {},
                onDismiss = {}
            )
        }

    }

    LaunchedEffect(feed.loadState.append) {
        if (feed.loadState.append is LoadState.Error
            && (feed.loadState.append as LoadState.Error).error.toAppException() is AuthenticationException
        ) {
            onLogout()
            snackbarHost.showSnackbar(
                message = "You're logged out",
                actionLabel = null,
                onActionPerformed = {},
                onDismiss = {}
            )
        }
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = stringResource(R.string.feed),
            )
        },
        floatingActionButton = {
            FloatingButton(
                onClick = onNewPostAdd,
                icon = painterResource(R.drawable.add),
            )
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(WindowInsets.navigationBars)
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = feed.loadState.refresh is LoadState.Loading,
            onRefresh = feed::refresh,
            state = pullToRefreshState,
            modifier = Modifier.fillMaxSize()
        ) {
            when (feed.loadState.refresh) {
                is LoadState.Loading -> {
                    Loading()
                }

                !is LoadState.Loading if feed.itemCount == 0 -> {
                    EmptyScreen(onRefresh = feed::refresh)
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
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
                            count = feed.itemCount,
                            key = feed.itemKey { it.id }
                        ) { index ->
                            val item = feed[index]
                            if (item != null) {
                                PostListItem(
                                    post = item,
                                    onClick = onPostClick,
                                    onImageClick = onImageClick,
                                    onProfileClick = onProfileClick,
                                    onLikeClick = {
                                        feedViewModel.likePost(it)
                                    },
                                    onUnlikeClick = {
                                        feedViewModel.unlikePost(it)
                                    },
                                    isLiked = screenState.likedPosts.find { it == item.id } != null,
                                    isUnliked = screenState.unlikedPosts.find { it == item.id } != null,
                                )
                            }
                        }

                        loadState(feed.loadState.append, onRetryClick = feed::retry)
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyScreen(
    onRefresh: () -> Unit
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                stringResource(R.string.empty_feed),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            LightButton(
                onClick = onRefresh,
                text = stringResource(R.string.update)
            )
        }
    }
}



