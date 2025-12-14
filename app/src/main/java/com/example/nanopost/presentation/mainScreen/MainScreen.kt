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
import com.example.component.uicomponents.CustomSnackbar
import com.example.component.uicomponents.CustomSnackbarHost
import com.example.component.uicomponents.LocalSnackbarHost
import com.example.feature.auth.presentation.authScreen.AuthScreen
import com.example.feature.editprofile.presentation.editProfileScreen.EditProfileScreen
import com.example.feature.image.presentation.imageScreen.ImageScreen
import com.example.feature.image.presentation.imageScreen.ImageViewModel
import com.example.feature.subscribers.presentation.subscribersScreen.SubscribersScreen
import com.example.feature.subscribers.presentation.subscribersScreen.SubscribersViewModel
import com.example.nanopost.presentation.component.BottomNavigation
import com.example.nanopost.presentation.feedScreen.FeedScreen
import com.example.nanopost.presentation.imagesScreen.ImagesScreen
import com.example.nanopost.presentation.imagesScreen.ImagesViewModel
import com.example.nanopost.presentation.newPostScreen.NewPostScreen
import com.example.nanopost.presentation.postScreen.PostScreen
import com.example.nanopost.presentation.postScreen.PostViewModel
import com.example.nanopost.presentation.profilePostsScreen.ProfilePostsScreen
import com.example.nanopost.presentation.profilePostsScreen.ProfilePostsViewModel.ProfilePostsViewModelFactory
import com.example.nanopost.presentation.profileScreen.ProfileScreen
import com.example.nanopost.presentation.profileScreen.ProfileViewModel
import com.example.nanopost.presentation.searchProfilesScreen.SearchProfilesScreen


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
                            onLogout = mainViewModel::logout,
                            onProfileClick = {
                                backStack.add(Route.Profile(it))
                            },
                            onNewPostAdd = {
                                backStack.add(Route.NewPost)
                            },
                            onImageClick = {
                                backStack.add(Route.Image(it))
                            },
                            onPostClick = {
                                backStack.add(Route.Post(it))
                            },
                            onSearchClick = {
                                backStack.add(Route.SearchProfile)
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
                    entry<Route.Profile> { profile ->
                        val viewModel = hiltViewModel(
                            key = profile.profileId,
                            creationCallback = { factory: ProfileViewModel.ProfileViewModelFactory ->
                                factory.create(profile.profileId)
                            }
                        )
                        ProfileScreen(
                            profileViewModel = viewModel,
                            isUserProfile = profile.profileId == null,
                            onProfileEditClick = {
                                backStack.add(Route.EditProfile)
                            },
                            onBackClick = {
                                backStack.removeAt(backStack.lastIndex)
                            },
                            onImageClick = {
                                backStack.add(Route.Image(it))
                            },
                            onImagesClick = {
                                backStack.add(Route.Images(profile.profileId))
                            },
                            onSubscribersClick = {
                                backStack.add(Route.Subscribers(profile.profileId))
                            },
                            onPostClick = {
                                backStack.add(Route.Post(it))
                            },
                            onPostsClick = {
                                backStack.add(Route.ProfilePosts(profile.profileId))
                            },
                            onNewPostAdd = {
                                backStack.add(Route.NewPost)
                            },
                            onLogout = mainViewModel::logout

                        )
                    }
                    entry<Route.Empty> {}
                    entry<Route.ProfilePosts> { profile ->
                        val viewModel = hiltViewModel(
                            key = profile.profileId,
                            creationCallback = { factory: ProfilePostsViewModelFactory ->
                                factory.create(profile.profileId)
                            }
                        )
                        ProfilePostsScreen(
                            profilePostsViewModel = viewModel,
                            onImageClick = {
                                backStack.add(Route.Image(it))
                            },
                            onPostClick = {
                                backStack.add(Route.Post(it))
                            },
                            onBackClick = {
                                backStack.removeAt(backStack.lastIndex)
                            },
                            onLogout = mainViewModel::logout
                        )
                    }
                    entry<Route.Post> {
                        val viewModel = hiltViewModel(
                            key = it.postId,
                            creationCallback = { factory: PostViewModel.PostViewModelFactory ->
                                factory.create(it.postId)
                            }
                        )
                        PostScreen(
                            postViewModel = viewModel,
                            onBackClick = {
                                backStack.removeAt(backStack.lastIndex)
                            },
                            onLogout = mainViewModel::logout
                        )
                    }
                    entry<Route.Subscribers> { profile ->
                        val viewModel = hiltViewModel(
                            key = profile.profileId,
                            creationCallback = { factory: SubscribersViewModel.SubscribersViewModelFactory ->
                                factory.create(profile.profileId)
                            }
                        )
                        SubscribersScreen(
                            subscribersViewModel = viewModel,
                            onSubscriberClick = {
                                backStack.add(Route.Profile(it))
                            },
                            onBackClick = {
                                backStack.removeAt(backStack.lastIndex)
                            },
                            onLogout = mainViewModel::logout
                        )
                    }
                    entry<Route.Images> { profile ->
                        val viewModel = hiltViewModel(
                            key = profile.profileId,
                            creationCallback = { factory: ImagesViewModel.ImagesViewModelFactory ->
                                factory.create(profile.profileId)
                            }
                        )
                        ImagesScreen(
                            imagesViewModel = viewModel,
                            onBackClick = {
                                backStack.removeAt(backStack.lastIndex)
                            },
                            onImageClick = {
                                backStack.add(Route.Image(it))
                            },
                            onLogout = mainViewModel::logout
                        )
                    }
                    entry<Route.Image> {
                        val viewModel = hiltViewModel(
                            key = it.imageId,
                            creationCallback = { factory: ImageViewModel.ImageViewModelFactory ->
                                factory.create(it.imageId)
                            }
                        )
                        ImageScreen(
                            viewModel,
                            onBackClick = {
                                backStack.removeAt(backStack.lastIndex)
                            },
                            onLogout = mainViewModel::logout
                        )
                    }
                    entry<Route.EditProfile> {
                        EditProfileScreen(
                            onCloseClick = {
                                backStack.removeAt(backStack.lastIndex)
                            }
                        )
                    }
                    entry<Route.SearchProfile> {
                        SearchProfilesScreen(
                            onProfileClick = {
                                backStack.add(Route.Profile(it))
                            },
                            onLogout = mainViewModel::logout
                        )
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
        is Route.Profile if navKey.profileId == null -> NavigationOptions.PROFILE
        else -> null
    }
}