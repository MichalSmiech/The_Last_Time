package com.michasoft.thelasttime.eventList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.michasoft.thelasttime.model.Label

/**
 * Created by mśmiech on 29.09.2023.
 */

@Composable
fun DrawerContent(
    onMenuItemClick: (MenuItemType) -> Unit,
    labels: List<Label>,
    onLabelClick: (Label) -> Unit,
    onLabelsEditClick: () -> Unit,
    onAddNewLabelClick: () -> Unit
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
        Divider(
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        LabelsSection(
            labels = labels,
            onLabelClick = onLabelClick,
            onLabelsEditClick = onLabelsEditClick,
            onAddNewLabelClick = onAddNewLabelClick
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
            .padding(horizontal = 12.dp)
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

@Composable
fun LabelsSection(
    labels: List<Label>,
    onLabelClick: (Label) -> Unit,
    onLabelsEditClick: () -> Unit,
    onAddNewLabelClick: () -> Unit
) {
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
    LabelList(labels = labels, onLabelClick = onLabelClick)
    AddNewLabel(onClick = onAddNewLabelClick)
}

@Composable
fun LabelList(labels: List<Label>, onLabelClick: (Label) -> Unit) {
    LazyColumn(
        modifier = Modifier
    ) {
        items(labels) {
            LabelItemUI(it, onLabelClick)
        }
    }
}

@Composable
fun LabelItemUI(label: Label, onLabelClick: (Label) -> Unit) {
    Row(
        modifier = Modifier
            .clickable { onLabelClick(label) }
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(start = 8.dp)
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
                .padding(horizontal = 8.dp)
                .weight(1f), text = label.name
        )
    }
}

@Composable
fun AddNewLabel(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(start = 8.dp)
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
                .padding(horizontal = 8.dp)
                .weight(1f),
            text = "Create new label"
        )
    }
}