package com.michasoft.thelasttime.view

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat

/**
 * Created by mśmiech on 25.09.2023.
 */
@Composable
fun EditableTime(time: LocalTime, onTimeChange: (LocalTime) -> Unit) {
    val startTimePicker = createTimePicker(LocalContext.current, time) {
        onTimeChange(it)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(50.dp))
        TextButton(
            onClick = { startTimePicker.show() }
        ) {
            Text(
                text = time.toString(timeFormatter),
                color = MaterialTheme.colors.onSurface
            )
        }
    }
}

private fun createTimePicker(
    context: Context, time: LocalTime, onTimePicked: (LocalTime) -> Unit
): TimePickerDialog {
    val hour = time.hourOfDay
    val minute = time.minuteOfHour
    val timePicker = TimePickerDialog(
        context, { _, selectedHour: Int, selectedMinute: Int ->
            val pickedTime = LocalTime(selectedHour, selectedMinute)
            onTimePicked(pickedTime)
        }, hour, minute, true
    )
    return timePicker
}

private val timeFormatter = DateTimeFormat.forPattern("HH:mm")