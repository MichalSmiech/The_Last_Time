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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.michasoft.thelasttime.model.EventInstance
import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.format.PeriodFormatter
import org.joda.time.format.PeriodFormatterBuilder

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
        val periodText = if (instance.timestamp.isBeforeNow) {
            val period = Period(instance.timestamp, DateTime.now())
            period.toString(periodFormatter) + " ago"
        } else {
            val period = Period(DateTime.now(), instance.timestamp)
            period.toString(periodFormatter) + " until"
        }
        Text(text = periodText, modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp))
        Text(
            text = instance.timestamp.toString("EEE, dd MMM yyyy HH:mm"),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            fontSize = 14.sp
        )
    }
}

private val periodFormatter: PeriodFormatter = PeriodFormatterBuilder()
    .appendYears().appendSuffix(" year", " years")
    .appendSeparator(", ")
    .appendMonths().appendSuffix(" month", " months")
    .appendSeparator(", ")
    .appendDays().appendSuffix(" day", " days")
    .appendSeparator(", ")
    .appendHours().appendSuffix(" hour", " hours")
    .appendSeparator(", ")
    .appendMinutes().appendSuffix(" minute", " minutes")
    .toFormatter()