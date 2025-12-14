package com.example.component.uicomponents.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    secondaryContainer = SecondaryContainer,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    error = Error,
    inverseSurface = InverseSurface,
    inversePrimary = InversePrimary,
    inverseOnSurface = InverseOnSurface,
    outline = Outline,
    outlineVariant = OutlineVariant,
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    secondaryContainer = SecondaryContainer,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    error = Error,
    inverseSurface = InverseSurface,
    inversePrimary = InversePrimary,
    inverseOnSurface = InverseOnSurface,
    outline = Outline,
    outlineVariant = OutlineVariant,
)

@Composable
fun NanoPostTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    val extendedColors = ExtendedColors(
        surface1 = Surface1,
        surface2 = Surface2,
        surface3 = Surface3,
        surface5 = Surface5,
    )

    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

@Immutable
data class ExtendedColors(
    val surface1: Color,
    val surface2: Color,
    val surface3: Color,
    val surface5: Color,
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        surface1 = Color.Unspecified,
        surface2 = Color.Unspecified,
        surface3 = Color.Unspecified,
        surface5 = Color.Unspecified,
    )
}