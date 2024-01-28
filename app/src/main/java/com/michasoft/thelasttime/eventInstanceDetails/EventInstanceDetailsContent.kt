package com.michasoft.thelasttime.eventInstanceDetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    Column(modifier = Modifier.padding(start = 13.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(vertical = 13.dp),
                imageVector = Icons.Default.AccessTime,
                contentDescription = "date time icon"
            )
            Spacer(modifier = Modifier.width(13.dp))
            EditableDate(
                date = instance.timestamp.toLocalDate(),
                onDateChange = onDateChange
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(
                modifier = Modifier
                    .size(24.dp)
            )
            Spacer(modifier = Modifier.width(13.dp))
            EditableTime(
                time = instance.timestamp.toLocalTime(),
                onTimeChange = onTimeChange
            )
        }
    }
}