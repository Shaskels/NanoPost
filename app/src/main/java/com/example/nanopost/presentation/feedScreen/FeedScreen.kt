package com.example.nanopost.presentation.feedScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.nanopost.R
import com.example.nanopost.presentation.component.CustomTopBar
import com.example.nanopost.presentation.component.ErrorState
import com.example.nanopost.presentation.component.FloatingButton
import com.example.nanopost.presentation.component.LightButton
import com.example.nanopost.presentation.component.Loading
import com.example.nanopost.presentation.component.PostListItem
import com.example.nanopost.presentation.mainScreen.LocalSnackbarHost

@Composable
fun FeedScreen(
    onProfileClick: (String) -> Unit,
    onNewPostAdd: () -> Unit,
    onPostClick: (String) -> Unit,
    feedViewModel: FeedViewModel = hiltViewModel()
) {
    val screenState = feedViewModel.screenState.collectAsState()
    val snackbarHost = LocalSnackbarHost.current

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {

            when (val currentState = screenState.value) {
                FeedScreenState.Initial -> {}
                FeedScreenState.Loading -> Loading()
                is FeedScreenState.Content -> Screen(
                    currentState,
                    feedViewModel,
                    onProfileClick,
                    onPostClick
                )

                FeedScreenState.Error -> Error(feedViewModel)
            }
        }
    }
}

@Composable
fun Error(feedViewModel: FeedViewModel) {
    ErrorState(onRetryClick = { feedViewModel.getPosts() })
}

@Composable
fun Screen(
    screenState: FeedScreenState.Content,
    feedViewModel: FeedViewModel,
    onProfileClick: (String) -> Unit,
    onPostClick: (String) -> Unit
) {
    if (screenState.posts.isEmpty()) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    stringResource(R.string.empty_feed),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                LightButton(
                    onClick = {
                        feedViewModel.getPosts()
                    },
                    text = stringResource(R.string.update)
                )
            }
        }
    } else {
        PullToRefreshBox(
            isRefreshing = false,
            onRefresh = { feedViewModel.getPosts() },
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(items = screenState.posts, key = { it.id }) { item ->
                    PostListItem(
                        item,
                        onPostClick,
                        onProfileClick,
                        feedViewModel::likePost,
                        feedViewModel::unlikePost
                    )
                }
            }
        }
    }
}



