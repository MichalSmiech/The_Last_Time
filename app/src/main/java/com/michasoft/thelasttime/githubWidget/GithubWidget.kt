package com.michasoft.thelasttime.githubWidget

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.CircularProgressIndicator
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
import com.michasoft.thelasttime.model.DateRange
import com.michasoft.thelasttime.util.convertFloatRange
import com.michasoft.thelasttime.view.theme.AppTheme
import kotlinx.coroutines.runBlocking
import org.joda.time.LocalDate
import java.util.Locale

@Composable
fun GithubWidget(
    modifier: Modifier = Modifier,
    title: String,
    calendarModel: CalendarModel
) {
    Container(modifier = modifier, title = title, isLoading = calendarModel.isLoading) {
        Content(calendarModel = calendarModel)
    }
}

@Composable
private fun Container(
    modifier: Modifier,
    title: String,
    isLoading: Boolean,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(2.dp))
        Box(
            modifier = Modifier.border(
                1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                content()
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Legend()
                }
            }
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            MaterialTheme.colorScheme.scrim.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
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
        Text(text = "Less", color = MaterialTheme.colorScheme.outline)
        Day(normalizeValue = 0f)
        Day(normalizeValue = 0.25f)
        Day(normalizeValue = 0.5f)
        Day(normalizeValue = 0.75f)
        Day(normalizeValue = 1f)
        Text(text = "More", color = MaterialTheme.colorScheme.outline)
    }
}

@Composable
private fun Content(calendarModel: CalendarModel) {
    val listState = rememberLazyListState()
    val lastMonth = remember(calendarModel) {
        calendarModel.getLastMonth()
    }
    val padding = 4.dp
    LazyRow(
        modifier = Modifier.semantics {
            horizontalScrollAxisRange = ScrollAxisRange(value = { 0f }, maxValue = { 0f })
        },
        state = listState,
        reverseLayout = true,
        horizontalArrangement = Arrangement.spacedBy(padding)
    ) {
        items(calendarModel.totalMonthsInRange) {
            val month = calendarModel.minusMonth(
                from = lastMonth,
                subtractedMonthsCount = it
            )
            Month(padding = padding, month = month)
        }
    }
}

@Composable
private fun Month(
    modifier: Modifier = Modifier,
    padding: Dp,
    month: CalendarModel.CalendarMonth
) {
    Column(modifier = modifier) {
        Text(
            text = if (month.weeks.size >= 2) month.weeks.first().firstDay.toString("MMM", Locale.ENGLISH) else "",
            color = MaterialTheme.colorScheme.outline
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(padding)
        ) {
            month.weeks.forEach {
                Week(padding = padding, week = it)
            }
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
    val shape = RoundedCornerShape(3.dp)
    Box(
        modifier = Modifier
            .size(DaySize)
            .background(
                color = color,
                shape = shape
            )
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.10f), shape = shape)
    )
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MonthCountWidgetPreviewNight() {
    AppTheme {
        Surface {
            GithubWidget(
                modifier = Modifier.padding(horizontal = 16.dp),
                title = "Activity",
                calendarModel = CalendarModel(
                    DateRange(
                        LocalDate.now().minusYears(1),
                        LocalDate.now().minusDays(2)
                    ),
                    dateValueProvider = RandomDateValueProvider()
                ).apply { runBlocking { initDateValues() } }
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun MonthCountWidgetPreview() {
    AppTheme {
        Surface {
            GithubWidget(
                modifier = Modifier.padding(horizontal = 16.dp),
                title = "Activity",
                calendarModel = CalendarModel(
                    DateRange(
                        LocalDate.now().minusYears(1),
                        LocalDate.now().minusDays(2)
                    ),
                    dateValueProvider = RandomDateValueProvider()
                ).apply { runBlocking { initDateValues() } }
            )
        }
    }
}

private val DaySize = 12.dp