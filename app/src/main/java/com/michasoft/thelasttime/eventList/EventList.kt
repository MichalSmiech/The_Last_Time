package com.michasoft.thelasttime.eventList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.Label
import com.michasoft.thelasttime.util.periodText

/**
 * Created by mśmiech on 25.09.2023.
 */

@Composable
fun EventList(
    events: List<Event>,
    onEventClick: (String) -> Unit,
    onInstanceAdd: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(0.dp, 8.dp),
        contentPadding = PaddingValues(bottom = 60.dp)
    ) {
        items(events) {
            EventItem(it, onEventClick, onInstanceAdd)
        }
    }
}

@Composable
fun EventItem(event: Event, onClick: (String) -> Unit, onInstanceAdd: (String) -> Unit) {
    Row(
        modifier = Modifier
            .clickable { onClick(event.id) }
            .padding(top = 4.dp, bottom = 4.dp, start = 16.dp, end = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            val periodText = event.lastInstanceTimestamp?.periodText(lastTwo = true) ?: ""
            Text(
                text = event.name,
                modifier = Modifier.padding(end = 16.dp, top = 8.dp)
            )
            Text(
                text = periodText,
                modifier = Modifier.padding(end = 16.dp, bottom = 8.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontSize = 14.sp
            )
            if (event.labels.isNotEmpty()) {
                Labels(labels = event.labels)
            }
        }
        AddButton(onClick = { onInstanceAdd(event.id) })
    }
}

@Composable
fun AddButton(
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = "add icon"
        )
    }
}

@Composable
fun Labels(labels: List<Label>) {
    Row {
        labels.forEach {
            LabelItem(label = it)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelItem(label: Label) {
    Surface(
        shape = InputChipDefaults.shape,
    ) {
        Text(
            text = label.name,
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .defaultMinSize(minHeight = InputChipDefaults.Height)
        )
    }
}