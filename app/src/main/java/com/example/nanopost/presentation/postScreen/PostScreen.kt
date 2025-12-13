package com.example.nanopost.presentation.postScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.component.uicomponents.CustomDivider
import com.example.component.uicomponents.CustomTopBar
import com.example.component.uicomponents.ErrorState
import com.example.component.uicomponents.Loading
import com.example.nanopost.R
import com.example.nanopost.presentation.component.UserPostInfo
import com.example.shared.domain.entity.Post
import com.example.shared.network.domain.exceptions.AppException
import com.example.shared.network.domain.exceptions.AuthenticationException
import com.example.component.uicomponent.R as uiComponentsR

@Composable
fun PostScreen(postViewModel: PostViewModel, onBackClick: () -> Unit, onLogout: () -> Unit) {
    val screenState = postViewModel.screenState.collectAsState()

    LaunchedEffect(Unit) {
        postViewModel.getPost()
    }

    when (val currentState = screenState.value) {
        is PostScreenState.Content -> Screen(currentState.post, postViewModel, onBackClick)
        is PostScreenState.Error -> Error(currentState.e, postViewModel::getPost, onLogout)
        PostScreenState.Loading -> Loading()
    }
}

@Composable
fun Error(ex: AppException, onRetryClick: () -> Unit, onLogout: () -> Unit) {
    if (ex is AuthenticationException) {
        onLogout()
    } else {
        ErrorState(onRetryClick)
    }
}

@Composable
fun Screen(post: Post, postViewModel: PostViewModel, onBackClick: () -> Unit) {
    var isPostUser by remember { mutableStateOf(false) }
    var isShowMenu by remember { mutableStateOf(false) }

    LaunchedEffect(post.owner.id) {
        isPostUser = postViewModel.getUserId() == post.owner.id
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = stringResource(R.string.post),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    if (isPostUser) {
                        IconButton(onClick = { isShowMenu = !isShowMenu }) {
                            Icon(
                                painter = painterResource(R.drawable.more_vert),
                                contentDescription = null
                            )
                        }
                        DropdownMenu(
                            expanded = isShowMenu,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            onDismissRequest = { isShowMenu = false },
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        stringResource(R.string.delete),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                onClick = {
                                    postViewModel.deletePost()
                                    onBackClick()
                                }
                            )
                        }
                    }
                }
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
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item {
                UserPostInfo(
                    owner = post.owner,
                    dateCreated = post.dateCreated,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                CustomDivider()
            }

            item {
                if (post.text != null) {
                    Text(
                        text = post.text!!,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }

            items(items = post.images, key = { it.id }) { item ->
                AsyncImage(
                    model = item.sizes.first().url,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    placeholder = painterResource(uiComponentsR.drawable.no_photo),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}