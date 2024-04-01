package com.michasoft.thelasttime.eventList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material.icons.outlined.LogoDev
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.michasoft.thelasttime.model.Label

/**
 * Created by mśmiech on 29.09.2023.
 */

@Composable
fun DrawerContent(
    onMenuItemClick: (MenuItemType) -> Unit,
    activeMenuItem: MenuItemType? = null,
    labels: List<Label>,
    activeLabel: Label? = null,
    onLabelClick: (Label) -> Unit,
    onLabelsEditClick: () -> Unit,
    onAddNewLabelClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(280.dp)
            .verticalScroll(rememberScrollState())
    ) {
        DrawerHeader()
        MenuItem(
            type = MenuItemType.EVENTS,
            onMenuItemClick = onMenuItemClick,
            isActive = activeMenuItem == MenuItemType.EVENTS
        )
        LabelsSection(
            labels = labels,
            activeLabel = activeLabel,
            onLabelClick = onLabelClick,
            onLabelsEditClick = onLabelsEditClick,
            onAddNewLabelClick = onAddNewLabelClick
        )
        MenuItem(
            type = MenuItemType.SETTINGS,
            onMenuItemClick = onMenuItemClick,
            isActive = activeMenuItem == MenuItemType.SETTINGS
        )
        MenuItem(
            type = MenuItemType.DEBUG,
            onMenuItemClick = onMenuItemClick,
            isActive = activeMenuItem == MenuItemType.DEBUG
        )
        MenuItem(
            type = MenuItemType.SETTINGS,
            onMenuItemClick = onMenuItemClick,
            isActive = activeMenuItem == MenuItemType.SETTINGS
        )
        MenuItem(
            type = MenuItemType.SIGNOUT,
            onMenuItemClick = onMenuItemClick,
            isActive = activeMenuItem == MenuItemType.SIGNOUT
        )
        Spacer(modifier = Modifier.height(16.dp))
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
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = type.icon,
                    contentDescription = "menu item icon"
                )
            }
            Text(
                modifier = Modifier
                    .padding(horizontal = 0.dp)
                    .weight(1f),
                text = type.description,
                color = if (isActive) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (isActive) FontWeight.Bold else MaterialTheme.typography.labelLarge.fontWeight,
            )
        }
    }
}

enum class MenuItemType(val description: String, val icon: ImageVector) {
    EVENTS("Events", Icons.Outlined.Home),
    SETTINGS("Settings", Icons.Outlined.Settings),
    DEBUG("Debug", Icons.Outlined.LogoDev),
    SIGNOUT("Sign out", Icons.Outlined.Logout),
}

@Composable
fun LabelsSection(
    labels: List<Label>,
    activeLabel: Label? = null,
    onLabelClick: (Label) -> Unit,
    onLabelsEditClick: () -> Unit,
    onAddNewLabelClick: () -> Unit
) {
    if (labels.isEmpty()) {
        AddNewLabel(onClick = onAddNewLabelClick)
        return
    }
    Divider(
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier.padding(vertical = 8.dp)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Labels", style = MaterialTheme.typography.labelLarge)
        TextButton(onClick = onLabelsEditClick) {
            Text(
                text = "Edit",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
    LabelList(labels = labels, activeLabel = activeLabel, onLabelClick = onLabelClick)
    AddNewLabel(onClick = onAddNewLabelClick)
    Divider(
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun LabelList(labels: List<Label>, activeLabel: Label? = null, onLabelClick: (Label) -> Unit) {
    Column(
        modifier = Modifier
    ) {
        labels.forEach {
            LabelItemUI(it, onLabelClick, it == activeLabel)
        }
    }
}

@Composable
fun LabelItemUI(label: Label, onLabelClick: (Label) -> Unit, isActive: Boolean) {
    Surface(
        onClick = { onLabelClick(label) },
        shape = CircleShape,
        color = if (isActive) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Label,
                    contentDescription = "label icon"
                )
            }
            Text(
                modifier = Modifier
                    .padding(horizontal = 0.dp)
                    .weight(1f),
                text = label.name,
                color = if (isActive) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (isActive) FontWeight.Bold else MaterialTheme.typography.labelLarge.fontWeight,
            )
        }
    }
}

@Composable
fun AddNewLabel(onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "create label icon"
                )
            }
            Text(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .weight(1f),
                text = "Create new label",
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}