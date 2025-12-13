package com.example.nanopost.presentation.searchProfilesScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.component.uicomponents.ErrorState
import com.example.component.uicomponents.Loading
import com.example.component.uicomponents.SearchField
import com.example.component.uicomponents.loadState
import com.example.nanopost.presentation.extentions.toAppException
import com.example.nanopost.presentation.subscribersScreen.SubscriberItem
import com.example.shared.network.domain.exceptions.AuthenticationException

@Composable
fun SearchProfilesScreen(
    searchProfilesViewModel: SearchProfilesViewModel = hiltViewModel(),
    onProfileClick: (String) -> Unit,
    onLogout: () -> Unit,
) {
    val query by searchProfilesViewModel.query.collectAsState()
    val profiles = searchProfilesViewModel.profiles.collectAsLazyPagingItems()
    val pullToRefreshState = rememberPullToRefreshState()
    val listState = rememberLazyListState()

    Scaffold { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            SearchField(
                query = query,
                onQueryChange = searchProfilesViewModel::onQueryChange,
                onSearchClick = {},
                modifier = Modifier.fillMaxWidth()
            )

            PullToRefreshBox(
                isRefreshing = profiles.loadState.refresh is LoadState.Loading,
                onRefresh = profiles::refresh,
                state = pullToRefreshState,
                modifier = Modifier.fillMaxSize()
            ) {

                when (val state = profiles.loadState.refresh) {
                    is LoadState.Error -> {
                        if (state.error.toAppException() is AuthenticationException) {
                            onLogout()
                        } else {
                            ErrorState(profiles::retry)
                        }
                    }

                    is LoadState.Loading -> {
                        Loading()
                    }

                    else ->
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            items(
                                count = profiles.itemCount,
                                key = profiles.itemKey { it.id }) { index ->
                                val item = profiles[index]
                                if (item != null) {
                                    SubscriberItem(
                                        item,
                                        onProfileClick
                                    )
                                }
                            }

                            loadState(profiles.loadState.append, onRetryClick = profiles::retry)
                        }
                }
            }
        }
    }
}