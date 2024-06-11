package com.michasoft.thelasttime.view.theme

import android.view.Window
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.google.android.material.color.DynamicColors

@Composable
fun AppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    isDynamicColor: Boolean = false,
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

        isDarkTheme -> darkColorScheme
        else -> lightColorScheme
    }

    window?.statusBarColor = colorScheme.surface.toArgb()

    // Make use of Material3 imports
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography3,
        content = content
    )
}