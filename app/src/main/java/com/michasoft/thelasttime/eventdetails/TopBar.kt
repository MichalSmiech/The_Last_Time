package com.michasoft.thelasttime.eventdetails

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.runtime.Composable
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
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DiscardButton(onClick = onDiscardClick)
        NoShapeTextField(
            modifier = Modifier.weight(1f),
            singleLine = true,
            value = eventName,
            onValueChange = onEventNameChange,
            textStyle = LocalTextStyle.current.copy(fontSize = 20.sp)
        )
        DeleteButton(onClick = onDelete)
    }
}

@Composable
fun DiscardButton(
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.padding(8.dp, 8.dp)
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
        modifier = Modifier.padding(8.dp, 8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.DeleteOutline,
            contentDescription = "delete icon"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTopBar() {
    TopBar(eventName = "Podlewanie", onEventNameChange = {}, onDiscardClick = {}, onDelete = {})
}