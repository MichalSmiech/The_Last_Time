package com.michasoft.thelasttime.calendarWidget.monthCount

import org.joda.time.LocalDate

data class CalendarModel(
    val dateRange: DateRange,
    val dayValueProvider: DayValueProvider
) {
    val totalMonthsInRange: Int = calculateTotalMonthsInRange(dateRange)
    val monthsMap = mutableMapOf<Int, CalendarMonth>()
    val dayValues = mutableMapOf<LocalDate, Float>()

    init {
//        val startDate = dateRange.fromData.
    }

    private fun calculateTotalMonthsInRange(dateRange: DateRange): Int {
        val fromData = dateRange.fromData.withDayOfWeek(1)
        val toDate = dateRange.toDate
        if (fromData.year == toDate.year) {
            return toDate.monthOfYear - fromData.monthOfYear + 1
        }
        var months = 0
        months += MonthsInYear - fromData.monthOfYear + 1
        months += MonthsInYear * (toDate.year - fromData.year)
        months += toDate.monthOfYear
        return months
    }

    fun getLastMonth(): CalendarMonth {
        val lastWeek = getLastWeek()
        val firstDayOfMonth = lastWeek.firstDay.withDayOfMonth(1)
        return getMonth(firstDayOfMonth)
    }

    private fun getMonth(firstDayOfMonth: LocalDate): CalendarMonth {
        val firstDayOfFirstWeek = if (firstDayOfMonth.dayOfWeek != 1) {
            firstDayOfMonth.plusWeeks(1).withDayOfWeek(1)
        } else {
            firstDayOfMonth
        }

        val weeks = mutableListOf(CalendarWeek(firstDayOfFirstWeek))

        var week = CalendarWeek(weeks.first().firstDay.plusWeeks(1))
        val lastWeek = getLastWeek()
        while (week.firstDay.monthOfYear == firstDayOfMonth.monthOfYear && !week.firstDay.isAfter(lastWeek.firstDay)) {
            weeks.add(week)
            week = CalendarWeek(week.firstDay.plusWeeks(1))
        }
        return CalendarMonth(weeks)
    }

    private fun getLastWeek(): CalendarWeek {
        val firstDay = dateRange.toDate.withDayOfWeek(1)
        return CalendarWeek(
            firstDay = firstDay,
        )
    }

    fun minusMonth(from: CalendarMonth, subtractedMonthsCount: Int): CalendarMonth {
        if (subtractedMonthsCount == 0) {
            return from
        }
        return getMonth(from.weeks.first().firstDay.minusMonths(subtractedMonthsCount).withDayOfMonth(1))
    }

    inner class CalendarMonth(
        val weeks: List<CalendarWeek>
    )

    inner class CalendarWeek(
        val firstDay: LocalDate,
    ) {
        val daysCount: Int =
            if (firstDay.weekyear == dateRange.toDate.weekyear
                && firstDay.weekOfWeekyear == dateRange.toDate.weekOfWeekyear
            ) dateRange.toDate.dayOfWeek
            else 7

        fun getDay(index: Int): CalendarDay {
            return CalendarDay(firstDay.plusDays(index))
        }

        override fun toString(): String {
            return "firstDay=$firstDay"
        }
    }

    inner class CalendarDay(
        val date: LocalDate
    ) {
        /**
         * @return value between <0, 1>
         */
        fun getNormalizeValue(): Float {
            return 0f// dayValueProvider.getNormalizeValue(date)
        }
    }
}

data class DateRange(
    val fromData: LocalDate,
    val toDate: LocalDate
)

const val DaysInWeek: Int = 7
const val MonthsInYear: Int = 12