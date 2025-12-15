package com.examle.feature.images.presentation.imagesScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.example.component.uicomponents.CustomTopBar
import com.example.component.uicomponents.ErrorState
import com.example.component.uicomponents.Loading
import com.example.component.uicomponents.loadState
import com.example.feature.images.R
import com.example.shared.network.domain.exceptions.AuthenticationException
import com.example.shared.network.domain.exceptions.toAppException
import com.example.component.uicomponent.R as uiComponentsR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagesScreen(
    imagesViewModel: ImagesViewModel,
    onBackClick: () -> Unit,
    onImageClick: (String) -> Unit,
    onLogout: () -> Unit,
) {
    val images = imagesViewModel.images.collectAsLazyPagingItems()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(Unit) {
        images.refresh()
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = stringResource(R.string.images_big),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = images.loadState.refresh is LoadState.Loading,
            onRefresh = images::refresh,
            state = pullToRefreshState,
            modifier = Modifier.fillMaxSize()
        ) {
            when (val state = images.loadState.refresh) {
                is LoadState.Error -> {
                    if (state.error.toAppException() is AuthenticationException) {
                        onLogout()
                    } else {
                        ErrorState(images::retry)
                    }
                }

                is LoadState.Loading -> {
                    Loading()
                }

                else -> LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    contentPadding = paddingValues,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxSize()
                ) {
                    items(
                        count = images.itemCount,
                        key = images.itemKey { it.id }
                    ) { index ->
                        val item = images[index]
                        if (item != null) {
                            AsyncImage(
                                model = item.sizes.first().url,
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds,
                                placeholder = painterResource(uiComponentsR.drawable.no_photo),
                                modifier = Modifier
                                    .height(125.dp)
                                    .clickable(onClick = { onImageClick(item.id) })
                            )
                        }
                    }

                    loadState(images.loadState.append, images::retry)
                }
            }
        }
    }
}