package com.michasoft.thelasttime.eventAdd

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Created by mśmiech on 28.09.2023.
 */
@Composable
fun TopBar(
    onDiscardClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DiscardButton(onClick = onDiscardClick)
        Text(
            text = "Create new event",
            modifier = Modifier.weight(1f),
            maxLines = 1,
            fontSize = 20.sp
        )
        SaveButton(onClick = onSaveClick)
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
fun SaveButton(
    onClick: () -> Unit,
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.padding(8.dp, 8.dp)
    ) {
        Text(
            text = "Done"
        )
    }
}