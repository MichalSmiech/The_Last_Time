package com.michasoft.thelasttime.calendarWidget.monthCount

import org.joda.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class CalendarModelTest {
    lateinit var calendarModel: CalendarModel
    lateinit var dateRange: DateRange

    private fun setupCalendarModel(
        fromData: LocalDate,
        toDate: LocalDate
    ) {
        dateRange = DateRange(fromData, toDate)
        calendarModel = CalendarModel(dateRange, dateValueProvider = { 1f })
    }

    @Test
    fun getLastMonth() {
        setupCalendarModel(
            fromData = LocalDate(2023, 6, 12),
            toDate = LocalDate(2024, 6, 12),
        )
        val lastMonth = calendarModel.getLastMonth()

        assertEquals(dateRange.toDate.monthOfYear, lastMonth.weeks.last().firstDay.monthOfYear)
    }

    @Test
    fun totalMonthsInRange_1() {
        setupCalendarModel(
            fromData = LocalDate(2024, 6, 1),
            toDate = LocalDate(2024, 6, 12),
        )
        val totalMonthsInRange = calendarModel.totalMonthsInRange

        assertEquals(2, totalMonthsInRange)
    }

    @Test
    fun totalMonthsInRange_2() {
        setupCalendarModel(
            fromData = LocalDate(2024, 4, 1),
            toDate = LocalDate(2024, 4, 12),
        )
        val totalMonthsInRange = calendarModel.totalMonthsInRange

        assertEquals(1, totalMonthsInRange)
    }

    @Test
    fun totalMonthsInRange_3() {
        setupCalendarModel(
            fromData = LocalDate(2024, 4, 1),
            toDate = LocalDate(2024, 5, 6),
        )
        val totalMonthsInRange = calendarModel.totalMonthsInRange

        assertEquals(2, totalMonthsInRange)
    }

    @Test
    fun totalMonthsInRange_4() {
        setupCalendarModel(
            fromData = LocalDate(2024, 4, 1),
            toDate = LocalDate(2024, 6, 3),
        )
        val totalMonthsInRange = calendarModel.totalMonthsInRange

        assertEquals(3, totalMonthsInRange)
    }

    @Test
    fun totalMonthsInRange_5() {
        setupCalendarModel(
            fromData = LocalDate(2023, 6, 27),
            toDate = LocalDate(2023, 6, 30),
        )
        val totalMonthsInRange = calendarModel.totalMonthsInRange

        assertEquals(1, totalMonthsInRange)
    }

    @Test
    fun totalMonthsInRange_6() {
        setupCalendarModel(
            fromData = LocalDate(2023, 12, 25),
            toDate = LocalDate(2024, 1, 1),
        )
        val totalMonthsInRange = calendarModel.totalMonthsInRange

        assertEquals(2, totalMonthsInRange)
    }

    @Test
    fun totalMonthsInRange_7() {
        setupCalendarModel(
            fromData = LocalDate(2023, 1, 1),
            toDate = LocalDate(2024, 1, 1),
        )
        val totalMonthsInRange = calendarModel.totalMonthsInRange

        assertEquals(14, totalMonthsInRange)
    }

    @Test
    fun minusMonth_1() {
        setupCalendarModel(
            fromData = LocalDate(2023, 6, 27),
            toDate = LocalDate(2024, 6, 12),
        )
        val lastMonth = calendarModel.getLastMonth()
        val minusMonth = calendarModel.minusMonth(lastMonth, 1)

        assertEquals(calendarModel.dateRange.toDate.monthOfYear - 1, minusMonth.weeks.first().firstDay.monthOfYear)
    }

    @Test
    fun minusMonth_2() {
        setupCalendarModel(
            fromData = LocalDate(2024, 1, 1),
            toDate = LocalDate(2024, 2, 8),
        )
        val lastMonth = calendarModel.getLastMonth()
        val minusMonth = calendarModel.minusMonth(lastMonth, 1)

        assertEquals(calendarModel.dateRange.fromData, minusMonth.weeks.first().firstDay)
    }

    @Test
    fun checkFirstMonth() {
        setupCalendarModel(
            fromData = LocalDate(2023, 6, 27),
            toDate = LocalDate(2024, 6, 12),
        )
        val lastMonth = calendarModel.getLastMonth()
        val totalMonthsInRange = calendarModel.totalMonthsInRange
        val firstMonth = calendarModel.minusMonth(lastMonth, totalMonthsInRange - 1)

        assertEquals(
            dateRange.fromData.withDayOfWeek(1).monthOfYear,
            firstMonth.weeks.first().firstDay.monthOfYear
        )
    }

    @Test
    fun dateRange_1() {
        setupCalendarModel(
            fromData = LocalDate(2023, 6, 27),
            toDate = LocalDate(2024, 6, 12),
        )
        checkDateRange()
    }

    @Test
    fun dateRange_2() {
        setupCalendarModel(
            fromData = LocalDate(2023, 1, 1),
            toDate = LocalDate(2024, 1, 1),
        )
        checkDateRange()
    }

    private fun checkDateRange() {
        val lastMonth = calendarModel.getLastMonth()

        val weeks = mutableListOf<CalendarModel.CalendarWeek>()
        for (i in calendarModel.totalMonthsInRange - 1 downTo 1) {
            weeks.addAll(calendarModel.minusMonth(lastMonth, i).weeks)
        }
        weeks.addAll(lastMonth.weeks)

        val days = mutableSetOf<LocalDate>()
        weeks.forEach { week ->
            for (i in 0 until week.daysCount) {
                week.getDay(i).let {
                    assert(it.date !in days) { "day double ${it.date}" }
                    days.add(it.date)
                }
            }
        }

        var day = dateRange.fromData.withDayOfWeek(1)
        while (day <= dateRange.toDate) {
            assert(day in days) { "day not exists $day" }
            day = day.plusDays(1)
        }

        val fromData = dateRange.fromData.withDayOfWeek(1)
        days.forEach { day ->
            assert(fromData <= day && day <= dateRange.toDate) { "day is out of range (day=$day)" }
        }
    }

    @Test
    fun firstDay() {
        setupCalendarModel(
            fromData = LocalDate(2023, 6, 27),
            toDate = LocalDate(2024, 6, 12),
        )
        val lastMonth = calendarModel.getLastMonth()
        val totalMonthsInRange = calendarModel.totalMonthsInRange
        val firstMonth = calendarModel.minusMonth(lastMonth, totalMonthsInRange - 1)

        assertEquals(
            dateRange.fromData.withDayOfWeek(1),
            firstMonth.weeks.first().firstDay
        )
    }
}

const val DaysInWeek: Int = 7