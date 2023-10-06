package com.michasoft.thelasttime.eventDetails

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.michasoft.thelasttime.view.NoShapeTextField
import com.michasoft.thelasttime.view.theme.LastTimeTheme

/**
 * Created by mśmiech on 21.09.2023.
 */

@Composable
fun TopBar(
    eventName: String,
    onEventNameChange: (String) -> Unit,
    onDiscardClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onLabelsClick: () -> Unit
) {
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
        LabelsButton(onClick = onLabelsClick)
        DeleteButton(onClick = onDeleteClick)
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
            contentDescription = "discard icon"
        )
    }
}

@Composable
fun DeleteButton(
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.DeleteOutline,
            contentDescription = "delete icon"
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
            contentDescription = "delete icon"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTopBar() {
    LastTimeTheme {
        TopBar(
            eventName = "Podlewanie",
            onEventNameChange = {},
            onDiscardClick = {},
            onDeleteClick = {},
            onLabelsClick = {})
    }
}