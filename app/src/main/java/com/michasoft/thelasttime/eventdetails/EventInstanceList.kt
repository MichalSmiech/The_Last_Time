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
        val periodText = getPeriodText(instance.timestamp)
        Text(text = periodText, modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp))
        Text(
            text = instance.timestamp.toString("EEE, dd MMM yyyy HH:mm").capitalize(Locale.current),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            fontSize = 14.sp
        )
    }
}

private fun getPeriodText(timestamp: DateTime): String {
    var periodText = if (timestamp.isBeforeNow) {
        val period = Period(timestamp, DateTime.now())
        period.toString(periodFormatter).ifBlank { "moments" } + " ago"
    } else {
        val period = Period(DateTime.now(), timestamp)
        period.toString(periodFormatter).ifBlank { "moments" } + " until"
    }
    if (periodText.contains("week")) {
        periodText = changeWeeksToDays(periodText)
    }
    return periodText
}

private fun changeWeeksToDays(periodText1: String): String {
    var periodText = periodText1
    val weeksMatchResult = """, (\d+) (weeks|week)""".toRegex().find(periodText)!!
    if (periodText.contains("day")) {
        periodText = periodText.removeRange(weeksMatchResult.range)
        val daysMatchResult = """, (\d+) (days|day)""".toRegex().find(periodText)!!
        val days =
            weeksMatchResult.groups[1]!!.value.toInt() * 7 + daysMatchResult.groups[1]!!.value.toInt()
        periodText = periodText.replace(daysMatchResult.value, ", $days days")
    } else {
        val days = weeksMatchResult.groups[1]!!.value.toInt() * 7
        periodText = periodText.replace(weeksMatchResult.value, ", $days days")
    }
    return periodText
}

private val periodFormatter: PeriodFormatter = PeriodFormatterBuilder()
    .appendYears().appendSuffix(" year", " years")
    .appendSeparator(", ")
    .appendMonths().appendSuffix(" month", " months")
    .appendSeparator(", ")
    .appendWeeks().appendSuffix(" week", " weeks")
    .appendSeparator(", ")
    .appendDays().appendSuffix(" day", " days")
    .appendSeparator(", ")
    .appendHours().appendSuffix(" hour", " hours")
    .appendSeparator(", ")
    .appendMinutes().appendSuffix(" minute", " minutes")
    .toFormatter()