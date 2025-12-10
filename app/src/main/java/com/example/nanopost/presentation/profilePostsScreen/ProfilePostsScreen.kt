package com.example.nanopost.presentation.profilePostsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.nanopost.R
import com.example.nanopost.presentation.component.CustomTopBar
import com.example.nanopost.presentation.component.PostListItem
import com.example.nanopost.presentation.component.loadState

@Composable
fun ProfilePostsScreen(
    profilePostsViewModel: ProfilePostsViewModel,
    isUserProfile: Boolean,
    onPostClick: (String) -> Unit,
    onBackClick: () -> Unit,
) {
    val posts = profilePostsViewModel.posts.collectAsLazyPagingItems()

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
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                bottom = paddingValues.calculateBottomPadding() + 16.dp,
                top = paddingValues.calculateTopPadding() + 16.dp,
                start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                end = paddingValues.calculateEndPadding(LayoutDirection.Ltr)
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
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
                        onProfileClick = {},
                        onLikeClick = {
                            if (!isUserProfile){
                                profilePostsViewModel.likePost(it)
                            }
                        },
                        onUnlikeClick = {
                            if (!isUserProfile){
                                profilePostsViewModel.unlikePost(it)
                            }
                        },
                    )
                }
            }

            loadState(posts.loadState.append, onRetryClick = posts::retry)
        }
    }
}