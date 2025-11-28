package com.example.nanopost.presentation.feedScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import com.example.nanopost.domain.entity.Post
import com.example.nanopost.presentation.component.FloatingButton
import com.example.nanopost.presentation.component.LikeButton
import com.example.nanopost.presentation.component.UserPostInfo
import com.example.nanopost.presentation.mainScreen.LocalSnackbarHost
import com.example.nanopost.presentation.mainScreen.showSnackbar
import com.example.nanopost.presentation.theme.LocalExtendedColors

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
                    is FeedScreenState.Content -> Screen(currentState)
                    FeedScreenState.Error -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) { }
                        snackbarHost.showSnackbar(
                            message = stringResource(R.string.failed_to_load),
                            actionLabel = stringResource(R.string.retry),
                            onActionPerformed = { feedViewModel.getPosts() },
                            onDismiss = { }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Loading() {
    Column(Modifier.fillMaxSize()) {
        Spacer(Modifier.weight(1f))

        CircularProgressIndicator(
            modifier = Modifier
                .width(64.dp)
                .align(Alignment.CenterHorizontally),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )

        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun Screen(screenState: FeedScreenState.Content) {
    if (screenState.posts.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) { }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(items = screenState.posts, key = { it.id }) { item ->
                PostListItem(item)
            }
        }
    }
}

@Composable
fun PostListItem(post: Post) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardColors(
            containerColor = LocalExtendedColors.current.surface1,
            contentColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContainerColor = LocalExtendedColors.current.surface1,
            disabledContentColor = MaterialTheme.colorScheme.surfaceVariant
        )
    )
    {
        UserPostInfo(post = post, modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp))

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.surfaceVariant
        )

        if (post.text != null) {
            Text(
                post.text,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
            )
        }

        LikeButton(
            onClick = {},
            likesCount = post.likes.likesCount,
            liked = post.likes.liked,
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
        )

    }
}

