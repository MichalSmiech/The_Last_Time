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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Repeat
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
import com.michasoft.thelasttime.githubWidget.CalendarModel
import com.michasoft.thelasttime.githubWidget.GithubWidget
import com.michasoft.thelasttime.githubWidget.RandomDateValueProvider
import com.michasoft.thelasttime.model.DateRange
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.EventInstanceField
import com.michasoft.thelasttime.model.EventInstanceSchema
import com.michasoft.thelasttime.model.Label
import com.michasoft.thelasttime.model.reminder.Reminder
import com.michasoft.thelasttime.model.reminder.SingleReminder
import com.michasoft.thelasttime.view.theme.AppTheme
import kotlinx.coroutines.runBlocking
import org.joda.time.DateTime
import org.joda.time.LocalDate

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
    activityCalendarModel: CalendarModel,
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
        Spacer(modifier = Modifier.height(8.dp))
        GithubWidget(
            modifier = Modifier.padding(horizontal = 16.dp),
            title = "Activity",
            calendarModel = activityCalendarModel
        )
        Spacer(modifier = Modifier.height(8.dp))
        EventInstanceList(
            instances = eventInstances,
            onEventInstanceClick = onEventInstanceClick
        )
    }
}

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

@Composable
fun ReminderItem(reminder: Reminder, onClick: (String) -> Unit) {
    Surface(
        shape = InputChipDefaults.shape,
        color = if (reminder.isTriggerDateTimePassed) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surfaceVariant,
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
    val reminder =
        SingleReminder("", "", DateTime.now().plusHours(-1), DateTime.now().plusHours(1), reshowEnabled = true)
    AppTheme {
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
            activityCalendarModel = CalendarModel(
                DateRange(
                    LocalDate.now().minusYears(1),
                    LocalDate.now()
                ),
                dateValueProvider = RandomDateValueProvider()
            ).apply { runBlocking { initDateValues() } },
            onEventInstanceClick = {}
        )
    }
}