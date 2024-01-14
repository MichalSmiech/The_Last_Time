package com.michasoft.thelasttime.eventDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.michasoft.thelasttime.model.reminder.Reminder
import com.michasoft.thelasttime.view.EditableDate
import com.michasoft.thelasttime.view.EditableTime
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.joda.time.LocalDate

@Composable
fun EditReminderDialog(
    onDismiss: () -> Unit,
    eventId: String,
    reminderId: String? = null
) {
    val viewModel = viewModel<EditReminderViewModel>(
        factory = EditReminderViewModel.Factory(
            eventId,
            reminderId
        )
    )
    val scope = rememberCoroutineScope()

    val type = viewModel.type.collectAsState().value
    val typeIndex = when (type) {
        Reminder.Type.Single -> 0
        Reminder.Type.Repeated -> 1
    }
    LaunchedEffect(Unit) {
        viewModel.actions.onEach {
            when (it) {
                is EditReminderAction.Finish -> onDismiss()
            }
        }.launchIn(scope)
    }
    Dialog(onDismissRequest = onDismiss) {
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
                    text = if (reminderId == null) "Add reminder" else "Edit reminder",
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
                        onClick = { viewModel.changeType(Reminder.Type.Single) },
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
                        onClick = { viewModel.changeType(Reminder.Type.Repeated) },
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
                        val dateTime = viewModel.dateTime.collectAsState().value
                        val dateTimeError =
                            viewModel.dateTimeError.collectAsState(initial = false).value
                        EditableDate(
                            date = dateTime.toLocalDate(),
                            onDateChange = {
                                viewModel.changeDate(it)
                            },
                            minDate = LocalDate.now()
                        )
                        EditableTime(time = dateTime.toLocalTime(), onTimeChange = {
                            viewModel.changeTime(it)
                        })
                        if (dateTimeError) {
                            Text(
                                text = "Must be in future",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Buttons(
                            onSave = { viewModel.saveSingleReminder() },
                            onCancel = onDismiss
                        )
                    } else {
                        val periodText = viewModel.periodText.collectAsState().value
                        val periodTextError =
                            viewModel.periodTextError.collectAsState(initial = false).value
                        var showError by remember { mutableStateOf(false) }
                        Text(
                            text = "After creating an event instance,\nremind me in:",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        TextField(
                            value = periodText,
                            onValueChange = viewModel::changePeriodText,
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
                        Buttons(
                            onSave = {
                                showError = true
                                viewModel.saveRepeatedReminder()
                            },
                            onCancel = onDismiss
                        )
                    }

                }
            }
        }
    }
}

@Composable
private fun Buttons(
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Spacer(modifier = Modifier.height(24.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        TextButton(onClick = onCancel) {
            Text(text = "Cancel")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = onSave) {
            Text(text = "Save")
        }
    }
}