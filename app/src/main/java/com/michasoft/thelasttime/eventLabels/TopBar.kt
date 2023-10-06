package com.michasoft.thelasttime.eventLabels

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michasoft.thelasttime.view.NoShapeTextField

/**
 * Created by mśmiech on 06.10.2023.
 */
@Composable
fun TopBar(
    onDiscardClick: () -> Unit,
    filterLabelName: String,
    onFilterLabelNameChange: (String) -> Unit
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
            value = filterLabelName,
            onValueChange = onFilterLabelNameChange,
            placeholder = { Text(text = "Enter label name") }
        )
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