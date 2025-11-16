package com.example.nanopost.presentation.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.runtime.Composable
import com.example.nanopost.presentation.theme.LocalExtendedColors

@Composable
fun BottomNavigation() {
    NavigationBar(containerColor = LocalExtendedColors.current.surface2) {
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = {},
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