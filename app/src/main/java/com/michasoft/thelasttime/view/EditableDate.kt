package com.michasoft.thelasttime.view

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat

/**
 * Created by mśmiech on 25.09.2023.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditableDate(
    modifier: Modifier = Modifier,
    date: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    minDate: LocalDate? = null
) {
    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return minDate?.let { minDate ->
                !DateTime(utcTimeMillis, DateTimeZone.UTC).isBefore(minDate.toDateTime(LocalTime.MIDNIGHT))
            } ?: true
        }
    })
    val showDatePicker = rememberSaveable { mutableStateOf(false) }
    TextButton(
        modifier = modifier,
        onClick = {
            datePickerState.selectedDateMillis = date.toDateTime(LocalTime.now()).millis
            showDatePicker.value = true
        }) {
        Text(
            text = date.toString(dateFormatter).capitalize(Locale.current),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
    if (showDatePicker.value) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker.value = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedDate = DateTime(datePickerState.selectedDateMillis).toLocalDate()
                    onDateChange(selectedDate)
                    showDatePicker.value = false
                }) {
                    Text("Ok")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDatePicker.value = false
                }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

private val dateFormatter = DateTimeFormat.forPattern("EEE, dd MMM yyyy")