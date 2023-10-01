package com.michasoft.thelasttime.view.theme

import android.os.Build
import android.view.Window
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext

private val lightThemeColors = lightColorScheme(
)
private val darkThemeColors = lightColorScheme(
)

@Composable
fun LastTimeTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    isDynamicColor: Boolean = true,
    window: Window? = null,
    content: @Composable () -> Unit
) {
    val dynamicColor = isDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val colorScheme = when {
        dynamicColor && isDarkTheme -> {
            dynamicDarkColorScheme(LocalContext.current)
        }

        dynamicColor && !isDarkTheme -> {
            dynamicLightColorScheme(LocalContext.current)
        }

        isDarkTheme -> darkThemeColors
        else -> lightThemeColors
    }

    window?.statusBarColor = colorScheme.surface.toArgb()

    // Make use of Material3 imports
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography3,
        content = content
    )
}