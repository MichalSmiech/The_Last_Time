package com.michasoft.thelasttime.eventdetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.util.periodText

/**
 * Created by mśmiech on 21.09.2023.
 */
@Composable
fun EventInstanceList(
    instances: List<EventInstance>,
    onEventInstanceClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(0.dp, 8.dp),
    ) {
        items(instances) {
            EventInstanceItem(it, onEventInstanceClick)
        }
    }
}

@Composable
fun EventInstanceItem(instance: EventInstance, onClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .clickable { onClick(instance.id) }
            .fillMaxWidth()
    ) {
        val periodText = instance.timestamp.periodText(lastTwo = true)
        Text(text = periodText, modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp))
        Text(
            text = instance.timestamp.toString("EEE, dd MMM yyyy HH:mm").capitalize(Locale.current),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            fontSize = 14.sp
        )
    }
}