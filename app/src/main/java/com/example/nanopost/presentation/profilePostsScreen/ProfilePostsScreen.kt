package com.example.nanopost.presentation.profilePostsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.component.uicomponents.loadState
import com.example.nanopost.R
import com.example.nanopost.domain.exceptions.AuthenticationException
import com.example.nanopost.presentation.component.PostListItem
import com.example.nanopost.presentation.extentions.toAppException

@Composable
fun ProfilePostsScreen(
    profilePostsViewModel: ProfilePostsViewModel,
    onImageClick: (String) -> Unit,
    onPostClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onLogout: () -> Unit,
) {
    val likesState by profilePostsViewModel.screenState.collectAsState()
    val posts = profilePostsViewModel.posts.collectAsLazyPagingItems()
    val listState = rememberLazyListState()
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(Unit) {
        posts.refresh()
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = stringResource(R.string.posts_big),
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
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
            isRefreshing = posts.loadState.refresh is LoadState.Loading,
            onRefresh = posts::refresh,
            state = pullToRefreshState,
            modifier = Modifier.fillMaxSize()
        ) {

            when (val state = posts.loadState.refresh) {
                is LoadState.Error -> {
                    if (state.error.toAppException() is AuthenticationException) {
                        onLogout()
                    } else {
                        ErrorState(posts::retry)
                    }
                }

                is LoadState.Loading -> {
                    Loading()
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        state = listState,
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
                            count = posts.itemCount,
                            key = posts.itemKey { it.id }
                        ) { index ->
                            val item = posts[index]
                            if (item != null) {
                                PostListItem(
                                    post = item,
                                    onClick = onPostClick,
                                    onImageClick = onImageClick,
                                    onProfileClick = {},
                                    onLikeClick = profilePostsViewModel::likePost,
                                    onUnlikeClick = profilePostsViewModel::unlikePost,
                                    isLiked = likesState.likedPosts.find { it == item.id } != null,
                                    isUnliked = likesState.unlikedPosts.find { it == item.id } != null,
                                )
                            }
                        }

                        loadState(posts.loadState.append, onRetryClick = posts::retry)
                    }
                }
            }
        }
    }
}