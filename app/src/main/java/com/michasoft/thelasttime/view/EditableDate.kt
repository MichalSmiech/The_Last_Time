package com.michasoft.thelasttime.view

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
fun EditableDate(
    modifier: Modifier = Modifier,
    date: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    minDate: LocalDate? = null
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(vertical = 13.dp),
            imageVector = Icons.Default.AccessTime,
            contentDescription = "date time icon"
        )
        Spacer(modifier = Modifier.width(13.dp))
        val datePicker = createDatePicker(
            context = LocalContext.current,
            date = date,
            minDate = minDate,
            onDatePicked = {
                onDateChange(it)
            }
        )
        TextButton(onClick = { datePicker.show() }) {
            Text(
                text = date.toString(dateFormatter).capitalize(Locale.current),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

private fun createDatePicker(
    context: Context,
    date: LocalDate,
    onDatePicked: (LocalDate) -> Unit,
    minDate: LocalDate? = null
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
    if (minDate != null) {
        datePicker.datePicker.minDate = minDate.toDate().time
    }
    return datePicker
}

private val dateFormatter = DateTimeFormat.forPattern("EEE, dd MMM yyyy")