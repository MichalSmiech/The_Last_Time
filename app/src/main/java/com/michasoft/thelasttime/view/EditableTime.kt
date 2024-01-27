package com.michasoft.thelasttime.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat

/**
 * Created by mśmiech on 25.09.2023.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditableTime(
    modifier: Modifier = Modifier,
    time: LocalTime,
    onTimeChange: (LocalTime) -> Unit
) {
    val showTimePicker = rememberSaveable { mutableStateOf(false) }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .width(24.dp)
                .padding(vertical = 13.dp)
        )
        Spacer(modifier = Modifier.width(13.dp))
        TextButton(
            onClick = {
                showTimePicker.value = true
            }
        ) {
            Text(
                text = time.toString(timeFormatter),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
    if (showTimePicker.value) {
        val timePickerState = rememberTimePickerState(time.hourOfDay, time.minuteOfHour)
        TimePickerDialog(
            onCancel = { showTimePicker.value = false },
            onConfirm = {
                val pickedTime = LocalTime(timePickerState.hour, timePickerState.minute)
                onTimeChange(pickedTime)
                showTimePicker.value = false
            },
        ) {
            TimePicker(state = timePickerState)
        }
    }
}

private val timeFormatter = DateTimeFormat.forPattern("HH:mm")