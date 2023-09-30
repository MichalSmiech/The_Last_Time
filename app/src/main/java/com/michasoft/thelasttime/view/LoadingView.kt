package com.michasoft.thelasttime.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Created by mśmiech on 31.08.2023.
 */

@Composable
fun LoadingView(modifier: Modifier = Modifier, withBackground: Boolean = false) {
    if (withBackground) {
        Surface(
            modifier = modifier
                .fillMaxSize(),
            color = Color.Black.copy(alpha = 0.47f)
        ) {
            Box {
                LoadingIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    } else {
        Box(modifier = modifier) {
            LoadingIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier
            .width(60.dp)
            .height(60.dp)
    )
}