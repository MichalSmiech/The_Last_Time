package com.michasoft.thelasttime.eventList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Created by mśmiech on 29.09.2023.
 */

@Composable
fun DrawerContent(
    onMenuItemClick: (MenuItemType) -> Unit,
) {
    Column(
        modifier = Modifier.width(280.dp)
    ) {
        DrawerHeader()
        MenuItem(
            type = MenuItemType.EVENTS,
            onMenuItemClick = onMenuItemClick,
            isActive = true
        )
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
    Text(
        modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 16.dp),
        text = "Last Time",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun MenuItem(
    type: MenuItemType,
    onMenuItemClick: (MenuItemType) -> Unit,
    isActive: Boolean = false
) {
    Surface(
        onClick = { onMenuItemClick(type) },
        shape = CircleShape,
        color = if (isActive) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .padding(12.dp, 0.dp)
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                text = type.description,
                color = if (isActive) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (isActive) FontWeight.Bold else MaterialTheme.typography.labelLarge.fontWeight,
            )
        }

    }
}

enum class MenuItemType(val description: String) {
    EVENTS("Events"),
    SETTINGS("Settings"),
    DEBUG("Debug"),
}