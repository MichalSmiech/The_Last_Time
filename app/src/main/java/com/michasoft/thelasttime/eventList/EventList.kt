package com.michasoft.thelasttime.eventList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.font.FontWeight
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
            modifier = Modifier
                .weight(1f)
                .padding(top = 8.dp, bottom = 8.dp)
        ) {
            Text(
                text = event.name,
            )
            val periodText = event.lastInstanceTimestamp?.periodText(lastTwo = true) ?: ""
            if (periodText.isNotBlank()) {
                Text(
                    text = periodText,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
            }
            if (event.labels.isNotEmpty()) {
                Labels(modifier = Modifier.padding(top = 8.dp), labels = event.labels)
            }
        }
        AddButton(modifier = Modifier.padding(start = 16.dp), onClick = { onInstanceAdd(event.id) })
    }
}

@Composable
fun AddButton(
    modifier: Modifier,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = modifier,
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
fun Labels(modifier: Modifier, labels: List<Label>) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
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
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Box(
            modifier = Modifier.defaultMinSize(minHeight = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label.name,
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.sp
            )
        }
    }
}