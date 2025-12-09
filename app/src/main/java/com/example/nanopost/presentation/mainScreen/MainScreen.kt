package com.example.nanopost.presentation.mainScreen

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.nanopost.presentation.authScreen.AuthScreen
import com.example.nanopost.presentation.component.BottomNavigation
import com.example.nanopost.presentation.component.CustomSnackbar
import com.example.nanopost.presentation.feedScreen.FeedScreen
import com.example.nanopost.presentation.newPostScreen.NewPostScreen
import com.example.nanopost.presentation.postScreen.PostScreen
import com.example.nanopost.presentation.profilePostsScreen.ProfilePostsScreen
import com.example.nanopost.presentation.profilePostsScreen.ProfilePostsViewModel.ProfilePostsViewModelFactory
import com.example.nanopost.presentation.profileScreen.ProfileScreen
import com.example.nanopost.presentation.profileScreen.ProfileViewModel
import com.example.nanopost.presentation.postScreen.PostViewModel.PostViewModelFactory

val LocalSnackbarHost = compositionLocalOf<CustomSnackbarHost> {
    error("No Snackbar Host State")
}

@Composable
fun MainScreen(mainViewModel: MainViewModel) {
    val isUserAuth by mainViewModel.isUserAuth.collectAsState()

    val backStack = rememberNavBackStack(Route.Empty)
    val selectedTab = getSelectedTab(backStack.lastOrNull())

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarHost = remember { CustomSnackbarHost(scope, snackbarHostState) }

    LaunchedEffect(isUserAuth) {
        if (isUserAuth == true) {
            backStack.clearAndAdd(Route.Feed)
        } else if (isUserAuth == false) {
            backStack.clearAndAdd(Route.Auth)
        }
    }

    CompositionLocalProvider(LocalSnackbarHost provides snackbarHost) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) {
                    CustomSnackbar(
                        snackbarData = it,
                    )
                }
            },
            bottomBar = {
                if (selectedTab != null) {
                    BottomNavigation(
                        navigationOptions = NavigationOptions.entries,
                        selectedNavigationOption = selectedTab,
                        onItemClicked = {
                            when (it) {
                                NavigationOptions.FEED -> {
                                    backStack.clearAndAdd(Route.Feed)
                                }

                                NavigationOptions.PROFILE -> {
                                    backStack.clearAndAdd(Route.Profile(null))
                                }
                            }
                        }
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.background,
            contentWindowInsets = WindowInsets()
        ) { paddingValues ->
            NavDisplay(
                backStack = backStack,
                entryProvider = entryProvider {
                    entry<Route.Auth> {
                        AuthScreen(
                            onLogged = {
                                backStack.clearAndAdd(Route.Feed)
                            }
                        )
                    }
                    entry<Route.Feed> {
                        FeedScreen(
                            onNewPostAdd = {
                                backStack.add(Route.NewPost)
                            }
                        )
                    }
                    entry<Route.NewPost> {
                        NewPostScreen(
                            onClose = {
                                backStack.removeAt(backStack.lastIndex)
                            }
                        )
                    }
                    entry<Route.Profile> {
                        val viewModel = hiltViewModel(
                            creationCallback = { factory: ProfileViewModel.ProfileViewModelFactory ->
                                factory.create(it.profileId)
                            }
                        )
                        ProfileScreen(
                            profileViewModel = viewModel,
                            isUserProfile = it.profileId == null,
                            onPostsClick = {
                                backStack.add(Route.ProfilePosts(it.profileId))
                            },
                            onNewPostAdd = {
                                backStack.add(Route.NewPost)
                            },
                            onLogout = {
                                mainViewModel.logout()
                            }
                        )
                    }
                    entry<Route.Empty> {}
                    entry<Route.ProfilePosts> {
                        val viewModel = hiltViewModel(
                            creationCallback = { factory: ProfilePostsViewModelFactory ->
                                factory.create(it.profileId)
                            }
                        )
                        ProfilePostsScreen(
                            profilePostsViewModel = viewModel,
                            isUserProfile = it.profileId == null,
                            onBackClick = {
                                backStack.removeAt(backStack.lastIndex)
                            }
                        )
                    }
                    entry<Route.Post> {
                        val viewModel = hiltViewModel(
                            creationCallback = {factory: PostViewModelFactory ->
                                factory.create(it.postId)
                            }
                        )
                        PostScreen(viewModel)
                    }
                },
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

private fun NavBackStack<NavKey>.clearAndAdd(key: NavKey) {
    this.clear()
    this.add(key)
}

private fun getSelectedTab(navKey: NavKey?): NavigationOptions? {
    return when (navKey) {
        is Route.Feed -> NavigationOptions.FEED
        is Route.Profile -> NavigationOptions.PROFILE
        else -> null
    }
}