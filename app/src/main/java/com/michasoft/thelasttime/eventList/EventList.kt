package com.michasoft.thelasttime.eventList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.michasoft.thelasttime.model.Event
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
            .padding(top = 4.dp, bottom = 4.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            val periodText = event.lastInstanceTimestamp?.periodText(lastTwo = true) ?: ""
            Text(
                text = event.name,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)
            )
            Text(
                text = periodText,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                fontSize = 14.sp
            )
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
            tint = MaterialTheme.colors.primary,
            contentDescription = "add icon"
        )
    }
}