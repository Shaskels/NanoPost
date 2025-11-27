package com.example.nanopost.presentation.mainScreen

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.nanopost.presentation.authScreen.AuthScreen
import com.example.nanopost.presentation.component.BottomNavigation
import com.example.nanopost.presentation.feedScreen.FeedScreen
import com.example.nanopost.presentation.newPostScreen.NewPostScreen

@Composable
fun MainScreen(mainViewModel: MainViewModel = hiltViewModel()) {
    val backStack = rememberNavBackStack(Route.SplashScreen)
    val selectedTab = getSelectedTab(backStack.lastOrNull())

    LaunchedEffect(Unit) {
        if (mainViewModel.checkIfUserLogged()) {
            backStack.clearAndAdd(Route.Feed)
        } else {
            backStack.clearAndAdd(Route.Auth)
        }
    }

    Scaffold(
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
                                backStack.clearAndAdd(Route.Profile)
                            }
                        }
                    }
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(left = 0, right = 0, top = 0, bottom = 0)
    ) { paddingValues ->
        NavDisplay(
            backStack = backStack,
            entryProvider = entryProvider {
                entry<Route.Auth> {
                    AuthScreen(onLogged = {
                        backStack.clearAndAdd(Route.Feed)
                    })
                }
                entry<Route.Feed> {
                    FeedScreen(onNewPostAdd = {
                        backStack.add(Route.NewPost)
                    })
                }
                entry<Route.NewPost> {
                    NewPostScreen(
                        onClose = {
                            backStack.removeAt(backStack.lastIndex)
                        }
                    )
                }
                entry<Route.Profile> {

                }
                entry<Route.SplashScreen> {

                }
            },
            modifier = Modifier.padding(paddingValues)
        )
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