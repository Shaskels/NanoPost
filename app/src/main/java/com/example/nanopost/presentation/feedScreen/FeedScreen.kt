package com.example.nanopost.presentation.feedScreen

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.nanopost.presentation.component.FloatingButton
import com.example.nanopost.presentation.component.LightButton
import com.example.nanopost.presentation.component.Loading
import com.example.nanopost.presentation.component.PostListItem
import com.example.nanopost.presentation.mainScreen.CustomSnackbarHost
import com.example.nanopost.presentation.mainScreen.LocalSnackbarHost
import com.example.nanopost.presentation.mainScreen.showSnackbar

@Composable
fun FeedScreen(onNewPostAdd: () -> Unit, feedViewModel: FeedViewModel = hiltViewModel()) {
    val screenState = feedViewModel.screenState.collectAsState()
    val snackbarHost = LocalSnackbarHost.current

    Scaffold(
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
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                stringResource(R.string.feed),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .align(
                        Alignment.CenterHorizontally
                    )
                    .padding(vertical = 16.dp)
            )

            PullToRefreshBox(
                isRefreshing = false,
                onRefresh = { feedViewModel.getPosts() },
                modifier = Modifier.fillMaxSize()
            ) {
                when (val currentState = screenState.value) {
                    FeedScreenState.Initial -> {}
                    FeedScreenState.Loading -> Loading()
                    is FeedScreenState.Content -> Screen(currentState, feedViewModel)
                    FeedScreenState.Error -> Error(snackbarHost, feedViewModel)
                }
            }
        }
    }
}

@Composable
fun Error(snackbarHost: CustomSnackbarHost, feedViewModel: FeedViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        snackbarHost.showSnackbar(
            message = stringResource(R.string.failed_to_load),
            actionLabel = stringResource(R.string.retry),
            onActionPerformed = { feedViewModel.getPosts() },
            onDismiss = { }
        )
    }
}

@Composable
fun Screen(
    screenState: FeedScreenState.Content,
    feedViewModel: FeedViewModel
) {
    if (screenState.posts.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(items = screenState.posts, key = { it.id }) { item ->
                PostListItem(
                    item,
                    feedViewModel::likePost,
                    feedViewModel::unlikePost
                )
            }
        }
    }
}



