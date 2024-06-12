package com.michasoft.thelasttime.util

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.SnapPositionInLayout
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@ExperimentalFoundationApi
@Composable
fun rememberEndSnapFlingBehavior(lazyListState: LazyListState): FlingBehavior {
    val snappingLayout = remember(lazyListState) { SnapLayoutInfoProvider(lazyListState, EndToEnd) }
    return rememberSnapFlingBehavior(snappingLayout)
}

@OptIn(ExperimentalFoundationApi::class)
private val EndToEnd =
    SnapPositionInLayout { layoutSize, itemSize, beforeContentPadding, afterContentPadding, _ ->
        val availableLayoutSpace = layoutSize - beforeContentPadding - afterContentPadding
        beforeContentPadding
    }