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
        calendarModel = CalendarModel(dateRange)
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
    fun minusMonth() {
        setupCalendarModel(
            fromData = LocalDate(2023, 6, 27),
            toDate = LocalDate(2024, 6, 12),
        )
        val lastMonth = calendarModel.getLastMonth()
        val minusMonth = calendarModel.minusMonth(lastMonth, 1)

        assertEquals(calendarModel.dateRange.toDate.monthOfYear - 1, minusMonth.weeks.first().firstDay.monthOfYear)
    }

    @Test
    fun checkFirstMonth() {
        setupCalendarModel(
            fromData = LocalDate(2023, 6, 27),
            toDate = LocalDate(2024, 6, 12),
        )
        val lastMonth = calendarModel.getLastMonth()
        val totalMonthsInRange = calendarModel.totalMonthsInRange
        val firstMonth = calendarModel.minusMonth(lastMonth, totalMonthsInRange - 2)

        assertEquals(
            dateRange.fromData.monthOfYear,
            firstMonth.weeks.first().firstDay.plusDays(DaysInWeek - 1).monthOfYear
        )
    }
}

const val DaysInWeek: Int = 7