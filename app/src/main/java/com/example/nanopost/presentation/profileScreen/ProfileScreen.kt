package com.example.nanopost.presentation.profileScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.example.nanopost.R
import com.example.nanopost.domain.entity.Image
import com.example.nanopost.domain.entity.Post
import com.example.nanopost.domain.entity.Profile
import com.example.nanopost.domain.exceptions.AuthenticationException
import com.example.nanopost.presentation.component.CustomDialog
import com.example.nanopost.presentation.component.CustomDivider
import com.example.nanopost.presentation.component.CustomTopBar
import com.example.nanopost.presentation.component.DarkButton
import com.example.nanopost.presentation.component.ErrorState
import com.example.nanopost.presentation.component.FloatingButton
import com.example.nanopost.presentation.component.LightButton
import com.example.nanopost.presentation.component.Loading
import com.example.nanopost.presentation.component.NoPhotoAvatar
import com.example.nanopost.presentation.component.OutlinedButton
import com.example.nanopost.presentation.component.PhotoAvatar
import com.example.nanopost.presentation.component.PostListItem
import com.example.nanopost.presentation.component.loadState
import com.example.nanopost.presentation.extentions.toAppException
import com.example.nanopost.presentation.theme.LocalExtendedColors

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    isUserProfile: Boolean,
    onProfileEditClick: () -> Unit,
    onBackClick: () -> Unit,
    onImageClick: (String) -> Unit,
    onImagesClick: () -> Unit,
    onSubscribersClick: () -> Unit,
    onPostClick: (String) -> Unit,
    onPostsClick: () -> Unit,
    onNewPostAdd: () -> Unit,
    onLogout: () -> Unit
) {
    val screenState = profileViewModel.screenState.collectAsState()
    val posts = profileViewModel.posts.collectAsLazyPagingItems()
    var isAlertDialogShow by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        profileViewModel.getUserProfile()
        posts.refresh()
    }

    Scaffold(
        floatingActionButton = {
            if (isUserProfile) {
                FloatingButton(
                    onClick = onNewPostAdd,
                    icon = painterResource(R.drawable.add),
                )
            }
        },
        topBar = {
            CustomTopBar(
                title = stringResource(R.string.profile),
                navigationIcon = {
                    if (!isUserProfile) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                painter = painterResource(R.drawable.arrow_back),
                                contentDescription = null
                            )
                        }
                    }
                },
                actions = {
                    if (isUserProfile) {
                        IconButton(onClick = {
                            isAlertDialogShow = true
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.logout),
                                contentDescription = null
                            )
                        }
                    }
                }
            )
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(WindowInsets.navigationBars)
    ) { paddingValues ->

        if (isAlertDialogShow) {
            CustomDialog(
                onDismissRequest = { isAlertDialogShow = false },
                onConfirmButton = {
                    isAlertDialogShow = false
                    onLogout()
                },
                title = stringResource(R.string.logout),
                text = stringResource(R.string.are_you_sure_you_want_to_confirm)
            )
        }

        when (val currentState = screenState.value) {
            is ProfileScreenState.Content -> Screen(
                screenState = currentState,
                posts = posts,
                profileViewModel = profileViewModel,
                onProfileEditClick = onProfileEditClick,
                onImageClick = onImageClick,
                userProfile = isUserProfile,
                paddingValues = paddingValues,
                onImagesClick = onImagesClick,
                onSubscribersClick = onSubscribersClick,
                onPostClick = onPostClick,
                onPostsClick = onPostsClick,
                onLogout = onLogout
            )

            is ProfileScreenState.Error -> Error(
                currentState.errorType,
                profileViewModel::getUserProfile,
                onLogout
            )

            ProfileScreenState.Loading -> Loading()
        }
    }
}

@Composable
fun Error(errorType: ErrorType, onRetryClick: () -> Unit, onLogout: () -> Unit) {
    when (errorType) {
        ErrorType.AuthenticationError -> {
            onLogout()
        }

        else -> ErrorState(onRetryClick)
    }
}

@Composable
fun Screen(
    screenState: ProfileScreenState.Content,
    posts: LazyPagingItems<Post>,
    userProfile: Boolean,
    paddingValues: PaddingValues,
    profileViewModel: ProfileViewModel,
    onProfileEditClick: () -> Unit,
    onLogout: () -> Unit,
    onImageClick: (String) -> Unit,
    onImagesClick: () -> Unit,
    onSubscribersClick: () -> Unit,
    onPostClick: (String) -> Unit,
    onPostsClick: () -> Unit,
) {
    val pullToRefreshState = rememberPullToRefreshState()

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

            LoadState.Loading -> Loading()

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
                    item {
                        UserInfoCard(
                            profile = screenState.profile,
                            userProfile = userProfile,
                            onSubscribeClick = profileViewModel::subscribe,
                            onUnsubscribeClick = profileViewModel::unsubscribe,
                            isSubscribed = screenState.subscribed,
                            onImagesClick = onImagesClick,
                            onPostsClick = onPostsClick,
                            onSubscribersClick = onSubscribersClick,
                            onProfileEditClick = onProfileEditClick,
                        )
                    }

                    item {
                        ImagesCard(screenState.images, onImagesClick)
                    }

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
                                onLikeClick = profileViewModel::likePost,
                                onUnlikeClick = profileViewModel::unlikePost,
                                isLiked = screenState.likedPosts.find { it == item.id } != null,
                                isUnliked = screenState.unlikedPosts.find { it == item.id } != null,
                            )
                        }
                    }

                    loadState(posts.loadState.append, onRetryClick = posts::retry)
                }
            }
        }
    }
}

@Composable
fun UserInfoCard(
    profile: Profile,
    userProfile: Boolean,
    onProfileEditClick: () -> Unit,
    onSubscribeClick: (String) -> Unit,
    onUnsubscribeClick: (String) -> Unit,
    isSubscribed: Boolean?,
    onImagesClick: () -> Unit,
    onPostsClick: () -> Unit,
    onSubscribersClick: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardColors(
            containerColor = LocalExtendedColors.current.surface1,
            contentColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContainerColor = LocalExtendedColors.current.surface1,
            disabledContentColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            if (profile.avatarSmall == null) {
                NoPhotoAvatar(
                    profile.displayName ?: profile.username,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(64.dp)
                )
            } else {
                PhotoAvatar(
                    profile.avatarSmall,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(64.dp)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    profile.displayName ?: profile.username,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (profile.bio != null) {
                    Text(
                        profile.bio,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }

        CustomDivider()

        Row(modifier = Modifier.fillMaxWidth()) {
            InfoCard(
                value = profile.imagesCount.toString(),
                name = stringResource(R.string.images),
                onClick = onImagesClick,
                modifier = Modifier.weight(1f)
            )

            InfoCard(
                value = if (isSubscribed == true) "${profile.subscribersCount + 1}" else profile.subscribersCount.toString(),
                name = stringResource(R.string.subscribers),
                onClick = onSubscribersClick,
                modifier = Modifier.weight(1f)
            )

            InfoCard(
                value = profile.postsCount.toString(),
                name = stringResource(R.string.posts),
                onClick = onPostsClick,
                modifier = Modifier.weight(1f)
            )
        }

        CustomDivider()

        if (userProfile) {
            DarkButton(
                onClick = onProfileEditClick,
                text = stringResource(R.string.edit),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
        } else {
            if ((profile.subscribed && isSubscribed != false) || isSubscribed == true) {
                OutlinedButton(
                    onClick = { onUnsubscribeClick(profile.id) },
                    text = stringResource(R.string.unsubscribe),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            } else {
                LightButton(
                    onClick = { onSubscribeClick(profile.id) },
                    text = stringResource(R.string.subscribe),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
        }

    }
}

@Composable
fun InfoCard(value: String, name: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.clickable(
            onClick = onClick,
            interactionSource = interactionSource,
            indication = ripple(
                bounded = true,
                radius = 250.dp,
                color = LocalExtendedColors.current.surface1
            )
        )
    ) {
        Column(
            modifier = modifier
                .padding(16.dp)
        ) {
            Text(
                value,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.align(
                    Alignment.CenterHorizontally
                )
            )

            Text(
                name,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.align(
                    Alignment.CenterHorizontally
                )
            )
        }
    }
}

@Composable
fun ImagesCard(images: List<Image>, onImagesClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardColors(
            containerColor = LocalExtendedColors.current.surface1,
            contentColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContainerColor = LocalExtendedColors.current.surface1,
            disabledContentColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier.clickable(onClick = onImagesClick)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                stringResource(R.string.images_big),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(R.drawable.chevron_right),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            images.take(4).forEach {
                Image(it.sizes.first().url)
            }
        }
    }
}

@Composable
fun Image(url: String) {
    AsyncImage(
        model = url,
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(8.dp))
    )
}