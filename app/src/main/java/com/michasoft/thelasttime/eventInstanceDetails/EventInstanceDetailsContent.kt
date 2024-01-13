package com.michasoft.thelasttime.eventInstanceDetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
        EditableDate(
            modifier = Modifier.padding(start = 13.dp),
            date = instance.timestamp.toLocalDate(),
            onDateChange = onDateChange
        )
        EditableTime(
            modifier = Modifier.padding(start = 13.dp),
            time = instance.timestamp.toLocalTime(),
            onTimeChange = onTimeChange
        )
    }
}