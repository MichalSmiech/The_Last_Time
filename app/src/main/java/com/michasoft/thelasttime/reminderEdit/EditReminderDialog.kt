package com.michasoft.thelasttime.reminderEdit

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.michasoft.thelasttime.model.reminder.Reminder
import com.michasoft.thelasttime.userSessionComponent
import com.michasoft.thelasttime.view.EditableDate
import com.michasoft.thelasttime.view.EditableTime
import com.sebaslogen.resaca.viewModelScoped
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalTime

@Composable
fun EditReminderDialog(
    onDismiss: () -> Unit,
    initialState: EditReminderDialogInitialState,
) {
    val applicationContext = LocalContext.current.applicationContext
    val viewModel = viewModelScoped<EditReminderViewModel>(
        builder = { (applicationContext as Application).userSessionComponent().getEditReminderViewModel() }
    )
    LaunchedEffect(initialState) {
        viewModel.setData(
            initialState.eventId,
            initialState.reminderId
        )
    }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.actions.onEach {
            when (it) {
                EditReminderAction.Finish -> onDismiss()
            }
        }.launchIn(scope)
    }
    Dialog(onDismissRequest = onDismiss) {
        EditReminderContent(
            onDismiss = onDismiss,
            initialState = initialState,
            type = viewModel.type.collectAsState().value,
            onTypeChange = viewModel::changeType,
            dateTime = viewModel.dateTime.collectAsState().value,
            dateTimeError = viewModel.dateTimeError.collectAsState(initial = false).value,
            onDateChange = viewModel::changeDate,
            onTimeChange = viewModel::changeTime,
            onReminderSave = viewModel::saveReminder,
            onReminderDelete = viewModel::deleteReminder,
            periodText = viewModel.periodText.collectAsState().value,
            periodTextError = viewModel.periodTextError.collectAsState(initial = false).value,
            timeRangeEnabled = viewModel.timeRangeEnabled.collectAsState().value,
            timeRangeStart = viewModel.timeRangeStart.collectAsState().value,
            timeRangeEnd = viewModel.timeRangeEnd.collectAsState().value,
            timeRangeEndError = viewModel.timeRangeEndError.collectAsState(initial = false).value,
            onPeriodTextChange = viewModel::changePeriodText,
            onTimeRangeEnabledChange = viewModel::changeTimeRangeEnabled,
            onTimeRangeStartChange = viewModel::changeTimeRangeStart,
            onTimeRangeEndChange = viewModel::changeTimeRangeEnd,
        )
    }
}

@Composable
private fun EditReminderContent(
    onDismiss: () -> Unit,
    initialState: EditReminderDialogInitialState,
    type: Reminder.Type,
    onTypeChange: (Reminder.Type) -> Unit,
    dateTime: DateTime,
    dateTimeError: Boolean,
    onDateChange: (LocalDate) -> Unit,
    onTimeChange: (LocalTime) -> Unit,
    onReminderSave: () -> Unit,
    onReminderDelete: () -> Unit,
    periodText: String,
    periodTextError: Boolean,
    timeRangeEnabled: Boolean,
    timeRangeStart: LocalTime,
    timeRangeEnd: LocalTime,
    timeRangeEndError: Boolean,
    onPeriodTextChange: (String) -> Unit,
    onTimeRangeEnabledChange: (Boolean) -> Unit,
    onTimeRangeStartChange: (LocalTime) -> Unit,
    onTimeRangeEndChange: (LocalTime) -> Unit,
) {
    val typeIndex = when (type) {
        Reminder.Type.Single -> 0
        Reminder.Type.Repeated -> 1
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 24.dp)
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 24.dp),
                text = if (initialState.reminderId == null) "Add reminder" else "Edit reminder",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            TabRow(
                modifier = Modifier.fillMaxWidth(),
                selectedTabIndex = typeIndex,
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Tab(
                    selected = type == Reminder.Type.Single,
                    onClick = { onTypeChange(Reminder.Type.Single) },
                    text = {
                        Text(
                            text = "Single",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
                Tab(
                    selected = type == Reminder.Type.Repeated,
                    onClick = { onTypeChange(Reminder.Type.Repeated) },
                    text = {
                        Text(
                            text = "Repeated",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Column(Modifier.padding(horizontal = 24.dp)) {
                if (type == Reminder.Type.Single) {
                    SingleReminderContent(
                        dateTime = dateTime,
                        dateTimeError = dateTimeError,
                        onDateChange = onDateChange,
                        onTimeChange = onTimeChange
                    )
                } else {
                    RepeatedReminderContent(
                        periodText = periodText,
                        periodTextError = periodTextError,
                        timeRangeEnabled = timeRangeEnabled,
                        timeRangeStart = timeRangeStart,
                        timeRangeEnd = timeRangeEnd,
                        timeRangeEndError = timeRangeEndError,
                        onPeriodTextChange = onPeriodTextChange,
                        onTimeRangeEnabledChange = onTimeRangeEnabledChange,
                        onTimeRangeStartChange = onTimeRangeStartChange,
                        onTimeRangeEndChange = onTimeRangeEndChange,
                    )
                }
                Buttons(
                    onSave = { onReminderSave() },
                    onCancel = onDismiss,
                    onDelete = { onReminderDelete() },
                    deleteButtonShown = initialState.reminderId != null
                )
            }
        }
    }
}

@Composable
private fun SingleReminderContent(
    dateTime: DateTime,
    dateTimeError: Boolean,
    onDateChange: (LocalDate) -> Unit,
    onTimeChange: (LocalTime) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(vertical = 13.dp),
            imageVector = Icons.Default.AccessTime,
            contentDescription = "date time icon"
        )
        Spacer(modifier = Modifier.width(13.dp))
        EditableDate(
            date = dateTime.toLocalDate(),
            onDateChange = { onDateChange(it) },
            minDate = LocalDate.now()
        )
    }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .size(24.dp)
        )
        Spacer(modifier = Modifier.width(13.dp))
        EditableTime(
            time = dateTime.toLocalTime(),
            onTimeChange = { onTimeChange(it) }
        )
    }
    if (dateTimeError) {
        Text(
            text = "Must be in future",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun RepeatedReminderContent(
    periodText: String,
    periodTextError: Boolean,
    timeRangeEnabled: Boolean,
    timeRangeStart: LocalTime,
    timeRangeEnd: LocalTime,
    timeRangeEndError: Boolean,
    onPeriodTextChange: (String) -> Unit,
    onTimeRangeEnabledChange: (Boolean) -> Unit,
    onTimeRangeStartChange: (LocalTime) -> Unit,
    onTimeRangeEndChange: (LocalTime) -> Unit,
) {
    val showError by remember { mutableStateOf(false) }
    Text(
        text = "After creating an event instance,\nremind me in:",
        style = MaterialTheme.typography.bodyMedium
    )
    TextField(
        value = periodText,
        onValueChange = onPeriodTextChange,
        isError = showError && periodTextError,
        supportingText = {
            if (showError && periodTextError) {
                Text(text = "Invalid value")
            }
        },
        placeholder = {
            Text(text = "Enter period")
        }
    )
    Text(
        text = "y - year, m - month, w - week, d - day, h - hour, min - minute " +
                "\nExample: 1d 3h 15min",
        style = MaterialTheme.typography.bodySmall
    )
    Spacer(modifier = Modifier.height(16.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTimeRangeEnabledChange(timeRangeEnabled.not()) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = timeRangeEnabled,
            onCheckedChange = { onTimeRangeEnabledChange(it) })
        Text(text = "only between")
    }
    if (timeRangeEnabled) {
        Column(modifier = Modifier.padding(start = 24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(modifier = Modifier.widthIn(min = 37.dp), text = "from:")
                EditableTime(
                    time = timeRangeStart,
                    onTimeChange = { onTimeRangeStartChange(it) }
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(modifier = Modifier.widthIn(min = 37.dp), text = "to:")
                EditableTime(
                    time = timeRangeEnd,
                    onTimeChange = { onTimeRangeEndChange(it) }
                )
                if (timeRangeEndError) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun Buttons(
    onSave: () -> Unit,
    onCancel: () -> Unit,
    onDelete: () -> Unit,
    deleteButtonShown: Boolean
) {
    Spacer(modifier = Modifier.height(24.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        if (deleteButtonShown) {
            TextButton(onClick = onDelete) {
                Text(text = "Delete")
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        TextButton(onClick = onCancel) {
            Text(text = "Cancel")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = onSave) {
            Text(text = "Save")
        }
    }
}

data class EditReminderDialogInitialState(
    val eventId: String,
    val reminderId: String?
)

@Preview
@Composable
private fun EditReminderContentPreview() {
    EditReminderContent(
        onDismiss = {},
        initialState = EditReminderDialogInitialState("", ""),
        type = Reminder.Type.Repeated,
        onTypeChange = {},
        dateTime = DateTime.now(),
        dateTimeError = false,
        onDateChange = {},
        onTimeChange = {},
        onReminderSave = {},
        onReminderDelete = {},
        periodText = "",
        periodTextError = false,
        timeRangeEnabled = true,
        timeRangeEndError = true,
        timeRangeStart = LocalTime.now(),
        timeRangeEnd = LocalTime.now().plusHours(1),
        onPeriodTextChange = {},
        onTimeRangeEnabledChange = {},
        onTimeRangeStartChange = {},
        onTimeRangeEndChange = {},
    )
}