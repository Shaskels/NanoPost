package com.example.nanopost.presentation.component

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.component.uicomponents.theme.LocalExtendedColors
import com.example.nanopost.R
import com.example.nanopost.presentation.mainScreen.NavigationOptions

@Composable
fun BottomNavigation(
    navigationOptions: List<NavigationOptions>,
    selectedNavigationOption: NavigationOptions,
    onItemClicked: (NavigationOptions) -> Unit,
) {
    NavigationBar(containerColor = LocalExtendedColors.current.surface2) {
        for (option in navigationOptions) {
            NavigationBarItem(
                selected = selectedNavigationOption == option,
                onClick = { onItemClicked(option) },
                icon = {
                    Icon(
                        painter = getIcon(option, selectedNavigationOption == option),
                        null
                    )
                },
                label = {
                    Text(text = getLabel(option), style = MaterialTheme.typography.labelMedium)
                },
                colors = NavigationBarItemColors(
                    selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    selectedIndicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
            )
        }
    }
}

@Composable
private fun getIcon(option: NavigationOptions, selected: Boolean): Painter =
    when (option) {
        NavigationOptions.FEED -> if (selected)
            painterResource(R.drawable.view_stream_filled_yes)
        else painterResource(
            R.drawable.view_stream_filled_no)

        NavigationOptions.PROFILE -> if (selected)
            painterResource(R.drawable.account_circle_filled_yes)
        else painterResource(
            R.drawable.account_circle_filled_no)
    }

@Composable
private fun getLabel(option: NavigationOptions): String = stringResource(
    when (option) {
        NavigationOptions.FEED -> R.string.feed
        NavigationOptions.PROFILE -> R.string.profile
    }
)