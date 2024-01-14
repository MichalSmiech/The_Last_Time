package com.michasoft.thelasttime.eventDetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.outlined.AddAlert
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.michasoft.thelasttime.view.NoShapeTextField

/**
 * Created by mśmiech on 21.09.2023.
 */

@Composable
fun TopBar(
    eventName: String,
    onEventNameChange: (String) -> Unit,
    onDiscardClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onLabelsClick: () -> Unit,
    onAddReminderClick: () -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DiscardButton(onClick = onDiscardClick)
        NoShapeTextField(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            singleLine = true,
            value = eventName,
            onValueChange = onEventNameChange,
            textStyle = LocalTextStyle.current.copy(fontSize = 20.sp)
        )
        AddReminderButton(onClick = onAddReminderClick)
        Box() {
            MoreButton(onClick = {
                menuExpanded = !menuExpanded
            })
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false },
            ) {
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Label,
                            contentDescription = "labels"
                        )
                    },
                    text = {
                        Text("Labels")
                    },
                    onClick = {
                        menuExpanded = false
                        onLabelsClick()
                    },
                )
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "delete"
                        )
                    },
                    text = {
                        Text("Delete")
                    },
                    onClick = {
                        menuExpanded = false
                        onDeleteClick()
                    },
                )
            }

        }
    }
}

@Composable
fun DiscardButton(
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "discard"
        )
    }
}

@Composable
fun AddReminderButton(
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.AddAlert,
            contentDescription = "add reminder"
        )
    }
}

@Composable
fun LabelsButton(
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Label,
            contentDescription = "labels"
        )
    }
}

@Composable
fun MoreButton(
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.MoreVert,
            contentDescription = "more"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTopBar() {
    TopBar(
        eventName = "Podlewanie",
        onEventNameChange = {},
        onDiscardClick = {},
        onDeleteClick = {},
        onLabelsClick = {},
        onAddReminderClick = {})
}