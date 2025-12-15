package com.example.feature.searchprofiles.presentation.searchProfilesScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout

@Composable
fun ColumnWithFloatingFirstItem(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) {  measurables, constraints ->

        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }

        layout(constraints.maxWidth, height = constraints.maxHeight) {
            var yPosition = 0

            placeables.forEachIndexed { index, placeable ->
                if (index == 0) {
                    placeable.placeRelative(x = 0, y = yPosition, zIndex = 1f)
                    yPosition += placeable.height / 2
                } else {
                    placeable.placeRelative(x = 0, y = yPosition)
                    yPosition += placeable.height
                }
            }
        }
    }
}