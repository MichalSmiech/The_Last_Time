package com.michasoft.thelasttime.view

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

/**
 * Created by mśmiech on 25.09.2023.
 */
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
            Text(
                text = date.toString(dateFormatter).capitalize(Locale.current),
                color = MaterialTheme.colors.onSurface
            )
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

private val dateFormatter = DateTimeFormat.forPattern("EEE, dd MMM yyyy")