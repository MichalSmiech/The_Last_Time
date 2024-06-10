package com.michasoft.thelasttime.view.theme

import android.view.Window
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.google.android.material.color.DynamicColors

private val darkThemeColors = darkColorScheme(
    primary = DarkThemeColors.primary,
    onPrimary = DarkThemeColors.onPrimary,
    primaryContainer = DarkThemeColors.primaryContainer,
    onPrimaryContainer = DarkThemeColors.onPrimaryContainer,
    inversePrimary = DarkThemeColors.inversePrimary,
    secondary = DarkThemeColors.secondary,
    onSecondary = DarkThemeColors.onSecondary,
    secondaryContainer = DarkThemeColors.secondaryContainer,
    onSecondaryContainer = DarkThemeColors.onSecondaryContainer,
    tertiary = DarkThemeColors.tertiary,
    onTertiary = DarkThemeColors.onTertiary,
    tertiaryContainer = DarkThemeColors.tertiaryContainer,
    onTertiaryContainer = DarkThemeColors.onTertiaryContainer,
    background = DarkThemeColors.background,
    onBackground = DarkThemeColors.onBackground,
    surface = DarkThemeColors.surface,
    onSurface = DarkThemeColors.onSurface,
    surfaceVariant = DarkThemeColors.surfaceVariant,
    onSurfaceVariant = DarkThemeColors.onSurfaceVariant,
    surfaceTint = DarkThemeColors.surfaceTint,
    inverseSurface = DarkThemeColors.inverseSurface,
    inverseOnSurface = DarkThemeColors.inverseOnSurface,
    error = DarkThemeColors.error,
    onError = DarkThemeColors.onError,
    errorContainer = DarkThemeColors.errorContainer,
    onErrorContainer = DarkThemeColors.onErrorContainer,
    outline = DarkThemeColors.outline,
    outlineVariant = DarkThemeColors.outlineVariant,
    scrim = DarkThemeColors.scrim,
)

@Composable
fun LastTimeTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    isDynamicColor: Boolean = true,
    window: Window? = null,
    content: @Composable () -> Unit
) {
    val dynamicColor = isDynamicColor && DynamicColors.isDynamicColorAvailable()
    val colorScheme = when {
        dynamicColor && isDarkTheme -> {
            dynamicDarkColorScheme(LocalContext.current)
        }

        dynamicColor && !isDarkTheme -> {
            dynamicLightColorScheme(LocalContext.current)
        }

        isDarkTheme -> darkThemeColors
        else -> darkThemeColors
    }

    window?.statusBarColor = colorScheme.surface.toArgb()

    // Make use of Material3 imports
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography3,
        content = content
    )
}