package com.michasoft.thelasttime.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import org.joda.time.DateTime
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
    val datePickerState = rememberDatePickerState()
    val showDatePicker = rememberSaveable { mutableStateOf(false) }
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
        TextButton(onClick = {
            datePickerState.setSelection(date.toDateTime(LocalTime.now()).millis)
            showDatePicker.value = true
        }) {
            Text(
                text = date.toString(dateFormatter).capitalize(Locale.current),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
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
            DatePicker(
                state = datePickerState,
                dateValidator = { date ->
                    minDate?.let { minDate -> !DateTime(date).isBefore(minDate.toDateTime(LocalTime.MIDNIGHT)) } ?: true
                })
        }
    }
}

private val dateFormatter = DateTimeFormat.forPattern("EEE, dd MMM yyyy")