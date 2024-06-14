package com.michasoft.thelasttime.calendarWidget.monthCount

import org.joda.time.LocalDate
import kotlin.math.absoluteValue
import kotlin.random.Random

fun interface DayValueProvider {
    suspend fun getNormalizeValue(date: LocalDate): Float
}

class RandomDayValueProvider : DayValueProvider {
    override suspend fun getNormalizeValue(date: LocalDate): Float {
        return ((Random.nextInt().absoluteValue % 5).toFloat() / 4)
    }
}

