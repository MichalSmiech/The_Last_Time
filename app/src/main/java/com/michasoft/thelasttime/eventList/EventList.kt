package com.michasoft.thelasttime.eventList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Repeat
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
import com.michasoft.thelasttime.model.reminder.Reminder
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

@OptIn(ExperimentalLayoutApi::class)
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
            if (event.reminders.isNotEmpty() || event.labels.isNotEmpty()) {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    event.reminders.forEach { reminder ->
                        ReminderItem(reminder = reminder)
                    }
                    event.labels.forEach {
                        LabelItemUI(label = it)
                    }
                }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LabelItemUI(label: Label) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReminderItem(reminder: Reminder) {
    Surface(
        shape = InputChipDefaults.shape,
        color = if (reminder.isTriggerDateTimePassed) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Row(
            modifier = Modifier.defaultMinSize(minHeight = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val icon = when (reminder.type) {
                Reminder.Type.Single -> Icons.Outlined.Notifications
                Reminder.Type.Repeated -> Icons.Outlined.Repeat
            }
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .padding(start = 6.dp),
                imageVector = icon,
                contentDescription = "reminder"
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = reminder.label,
                modifier = Modifier
                    .padding(end = 8.dp),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.sp
            )
        }
    }
}