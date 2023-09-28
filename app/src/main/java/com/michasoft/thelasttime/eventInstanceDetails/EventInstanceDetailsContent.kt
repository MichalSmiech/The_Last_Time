package com.michasoft.thelasttime.eventInstanceDetails

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.view.EditableDate
import com.michasoft.thelasttime.view.EditableTime
import org.joda.time.LocalDate
import org.joda.time.LocalTime

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