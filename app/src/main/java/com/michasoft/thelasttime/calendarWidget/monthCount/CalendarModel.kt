package com.michasoft.thelasttime.calendarWidget.monthCount

import org.joda.time.LocalDate
import kotlin.math.absoluteValue
import kotlin.random.Random

data class CalendarModel(
    val dateRange: DateRange,
) {
    val totalWeeksInRange: Int = calculateTotalWeeksInRange(dateRange)

    private fun calculateTotalWeeksInRange(dateRange: DateRange): Int {
        if (dateRange.fromData.weekyear == dateRange.toDate.weekyear) {
            return dateRange.toDate.weekOfWeekyear - dateRange.fromData.weekOfWeekyear + 1
        }
        var weeks = 0
        weeks += WeeksInYear - dateRange.fromData.weekOfWeekyear + 1
        weeks += WeeksInYear * (dateRange.toDate.weekyear - dateRange.fromData.weekyear)
        weeks += WeeksInYear - dateRange.toDate.weekOfWeekyear + 1
        return weeks
    }

    fun getLastWeek(): CalendarWeek {
        val firstDay = dateRange.toDate.withDayOfWeek(1)
        return CalendarWeek(
            firstDay = firstDay,
            daysCount = dateRange.toDate.dayOfWeek
        )
    }

    fun minusWeeks(from: CalendarWeek, subtractedWeeksCount: Int): CalendarWeek {
        if (subtractedWeeksCount == 0) {
            return from
        }
        return CalendarWeek(firstDay = from.firstDay.minusWeeks(subtractedWeeksCount))
    }

    inner class CalendarWeek(
        val firstDay: LocalDate,
        val daysCount: Int = 7
    ) {
        fun getDay(index: Int): CalendarDay {
            return CalendarDay(this, firstDay.plusDays(index))
        }
    }

    inner class CalendarDay(
        val week: CalendarWeek,
        val date: LocalDate
    ) {
        /**
         * @return value between <0, 1>
         */
        fun getNormalizeValue(): Float {
            return ((Random.nextInt().absoluteValue % 5).toFloat() / 4)
        }
    }
}

data class DateRange(
    val fromData: LocalDate,
    val toDate: LocalDate
)

const val DaysInWeek: Int = 7
const val WeeksInYear: Int = 52