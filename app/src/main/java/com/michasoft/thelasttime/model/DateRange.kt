package com.michasoft.thelasttime.model

import org.joda.time.LocalDate

data class DateRange(
    val fromData: LocalDate,
    val toDate: LocalDate
) {
    init {
        if (fromData >= toDate) {
            throw IllegalArgumentException("fromData >= toDate! (fromData=${fromData}, toDate=$toDate)")
        }
    }

    fun getDates(): List<LocalDate> {
        val dates = mutableListOf<LocalDate>()
        var date = fromData
        while (date <= toDate) {
            dates.add(date)
            date = date.plusDays(1)
        }
        return dates
    }
}