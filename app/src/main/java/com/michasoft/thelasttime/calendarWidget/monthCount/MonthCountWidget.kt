package com.michasoft.thelasttime.calendarWidget.monthCount

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.ScrollAxisRange
import androidx.compose.ui.semantics.horizontalScrollAxisRange
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.michasoft.thelasttime.util.convertFloatRange
import com.michasoft.thelasttime.view.theme.AppTheme
import org.joda.time.LocalDate

@Composable
fun MonthCountWidget(
    calendarModel: CalendarModel
) {
    Container(title = "Activity") {
        Content(calendarModel = calendarModel)
    }
}

@Composable
private fun Container(
    title: String,
    content: @Composable () -> Unit
) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Text(text = title)
            content()
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Legend()
            }
        }
    }
}

@Composable
private fun Legend() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Less")
        Day(normalizeValue = 0f)
        Day(normalizeValue = 0.25f)
        Day(normalizeValue = 0.5f)
        Day(normalizeValue = 0.75f)
        Day(normalizeValue = 1f)
        Text(text = "More")
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Content(calendarModel: CalendarModel) {
    val listState = rememberLazyListState()
    val lastWeek = remember(calendarModel) {
        calendarModel.getLastWeek()
    }
    val padding = 4.dp
    LazyRow(
        modifier = Modifier.semantics {
            horizontalScrollAxisRange = ScrollAxisRange(value = { 0f }, maxValue = { 0f })
        },
        state = listState,
        flingBehavior = rememberSnapFlingBehavior(listState),
        reverseLayout = true,
        horizontalArrangement = Arrangement.spacedBy(padding)
    ) {
        items(calendarModel.totalWeeksInRange) {
            val week = calendarModel.minusWeeks(
                from = lastWeek,
                subtractedWeeksCount = it
            )
            Week(padding = padding, week = week)
        }
    }
}

@Composable
private fun Week(
    modifier: Modifier = Modifier,
    padding: Dp,
    week: CalendarModel.CalendarWeek,
) {
    Column(
        modifier = modifier.height(DaysInWeek * DaySize + padding * (DaysInWeek - 1)),
        verticalArrangement = Arrangement.spacedBy(padding)
    ) {
        repeat(week.daysCount) {
            Day(day = week.getDay(it))
        }
    }
}

@Composable
private fun Day(day: CalendarModel.CalendarDay) {
    Day(normalizeValue = day.getNormalizeValue())
}

@Composable
private fun Day(normalizeValue: Float) {
    val value = convertFloatRange(normalizeValue, 0f, 1f, 0.25f, 1f)
    val color = if (normalizeValue == 0f) {
        MaterialTheme.colorScheme.surfaceContainerHigh
    } else {
        MaterialTheme.colorScheme.primary.copy(alpha = value)
    }
    Box(
        modifier = Modifier
            .size(DaySize)
            .background(
                color = color,
                shape = RoundedCornerShape(4.dp)
            )
    )
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MonthCountWidgetPreview() {
    AppTheme {
        MonthCountWidget(
            calendarModel = CalendarModel(
                DateRange(
                    LocalDate.now().minusYears(1),
                    LocalDate.now().minusDays(2)
                )
            )
        )
    }
}

private val DaySize = 12.dp