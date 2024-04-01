package com.michasoft.thelasttime.eventDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.EventInstanceField
import com.michasoft.thelasttime.model.EventInstanceSchema
import com.michasoft.thelasttime.model.Label
import com.michasoft.thelasttime.model.reminder.Reminder
import com.michasoft.thelasttime.model.reminder.SingleReminder
import org.joda.time.DateTime

/**
 * Created by mśmiech on 21.09.2023.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EventDetailsContent(
    event: Event,
    eventInstances: List<EventInstance>,
    reminders: List<Reminder>,
    labels: List<Label>,
    onEventInstanceClick: (String) -> Unit,
    onLabelClick: () -> Unit,
    onReminderClick: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (reminders.isNotEmpty() || labels.isNotEmpty()) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 8.dp, end = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                reminders.forEach { reminder ->
                    ReminderItem(reminder = reminder, onClick = onReminderClick)
                }
                labels.forEach {
                    LabelItem(label = it, onClick = onLabelClick)
                }
            }
        }
        EventInstanceList(eventInstances, onEventInstanceClick)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelItem(label: Label, onClick: () -> Unit) {
    Surface(
        shape = InputChipDefaults.shape,
        color = MaterialTheme.colorScheme.surfaceVariant,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.defaultMinSize(minHeight = InputChipDefaults.Height),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label.name,
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderItem(reminder: Reminder, onClick: (String) -> Unit) {
    Surface(
        shape = InputChipDefaults.shape,
        color = if (reminder.isShownOrSkipped) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surfaceVariant,
        onClick = { onClick(reminder.id) }
    ) {
        Row(
            modifier = Modifier.defaultMinSize(minHeight = InputChipDefaults.Height),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val icon = when (reminder.type) {
                Reminder.Type.Single -> Icons.Outlined.Notifications
                Reminder.Type.Repeated -> Icons.Outlined.Repeat
            }
            Icon(
                modifier = Modifier.padding(start = 12.dp),
                imageVector = icon,
                contentDescription = "reminder"
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = reminder.label,
                modifier = Modifier
                    .padding(end = 16.dp),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventDetailsContentPreview() {
    val event = Event(
        "",
        "Podlewanie",
        DateTime.now(),
        EventInstanceSchema()
    )
    val eventInstances = listOf<EventInstance>(
        EventInstance(
            "",
            "",
            DateTime.now().minusMinutes(30),
            arrayListOf<EventInstanceField>()
        ),
        EventInstance(
            "",
            "",
            DateTime.now().minusHours(1).minusMinutes(10),
            arrayListOf<EventInstanceField>()
        ),
        EventInstance(
            "",
            "",
            DateTime.now().minusHours(3),
            arrayListOf<EventInstanceField>()
        ),
        EventInstance(
            "",
            "",
            DateTime.now().minusDays(1),
            arrayListOf<EventInstanceField>()
        ),
        EventInstance(
            "",
            "",
            DateTime.now().minusDays(1),
            arrayListOf<EventInstanceField>()
        ),
        EventInstance(
            "",
            "",
            DateTime.now().minusDays(1),
            arrayListOf<EventInstanceField>()
        ),
        EventInstance(
            "",
            "",
            DateTime.now().minusDays(1),
            arrayListOf<EventInstanceField>()
        ),
    )
    val reminder = SingleReminder("", "", DateTime.now().plusHours(-1), DateTime.now().plusHours(1))
    EventDetailsContent(
        event = event,
        eventInstances = eventInstances,
        reminders = listOf(reminder),
        labels = listOf(
            Label("", "work"),
            Label("", "home"),
            Label("", "gym"),
            Label("", "gym"),
            Label("", "gym"),
            Label("", "gym"),
        ),
        onReminderClick = {},
        onLabelClick = {},
        onEventInstanceClick = {}
    )
}