package com.michasoft.thelasttime.eventinstancedetails

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.michasoft.thelasttime.model.EventInstance
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat

/**
 * Created by mśmiech on 21.09.2023.
 */

@Composable
fun EventInstanceDetailsContent(
    instance: EventInstance,
    onDateChange: (LocalDate) -> Unit,
    onTimeChange: (LocalTime) -> Unit
) {
    Column {
        EditableDate(date = instance.timestamp.toLocalDate(), onDateChange = onDateChange)
        EditableTime(time = instance.timestamp.toLocalTime(), onTimeChange = onTimeChange)
    }
}

@Composable
fun EditableDate(date: LocalDate, onDateChange: (LocalDate) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(13.dp),
            imageVector = Icons.Default.AccessTime,
            contentDescription = "date time icon"
        )
        val datePicker = createDatePicker(LocalContext.current, date) {
            onDateChange(it)
        }
        TextButton(onClick = { datePicker.show() }) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = date.toString(dateFormatter).capitalize(Locale.current),
                    color = MaterialTheme.colors.onSurface
                )
            }
        }
    }
}

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
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = time.toString(timeFormatter),
                    color = MaterialTheme.colors.onSurface
                )
            }
        }
    }
}

private fun createDatePicker(
    context: Context, date: LocalDate, onDatePicked: (LocalDate) -> Unit
): DatePickerDialog {
    val year = date.year
    val month = date.monthOfYear - 1
    val dayOfMonth = date.dayOfMonth
    val datePicker = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            val pickedDate = LocalDate(selectedYear, selectedMonth + 1, selectedDayOfMonth)
            onDatePicked(pickedDate)
        },
        year,
        month,
        dayOfMonth
    )
    return datePicker
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

private val dateFormatter = DateTimeFormat.forPattern("EEE, dd MMM yyyy")
private val timeFormatter = DateTimeFormat.forPattern("HH:mm")