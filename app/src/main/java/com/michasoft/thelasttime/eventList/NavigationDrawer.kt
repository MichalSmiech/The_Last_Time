package com.michasoft.thelasttime.eventList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Created by mśmiech on 29.09.2023.
 */

@Composable
fun DrawerContent(
    onMenuItemClick: (MenuItemType) -> Unit,
) {
    DrawerHeader()
    Column {
        MenuItem(
            type = MenuItemType.SETTINGS,
            onMenuItemClick = onMenuItemClick
        )
        MenuItem(
            type = MenuItemType.DEBUG,
            onMenuItemClick = onMenuItemClick
        )
    }
}

@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .width(150.dp)
            .height(100.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Last Time", fontSize = 24.sp)
    }
}

@Composable
private fun MenuItem(
    type: MenuItemType,
    onMenuItemClick: (MenuItemType) -> Unit
) {
    Text(
        modifier = Modifier
            .clickable { onMenuItemClick(type) }
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .fillMaxWidth(),
        text = type.description
    )
}

enum class MenuItemType(val description: String) {
    SETTINGS("Settings"),
    DEBUG("Debug"),
}