package com.michasoft.thelasttime.calendarWidget.githubWidget

import org.joda.time.LocalDate
import kotlin.math.absoluteValue
import kotlin.random.Random

fun interface DateValueProvider {
    suspend fun getNormalizeValue(date: LocalDate): Float
}

class ZeroDateValueProvider : DateValueProvider {
    override suspend fun getNormalizeValue(date: LocalDate): Float {
        return 0f
    }
}

class RandomDateValueProvider : DateValueProvider {
    override suspend fun getNormalizeValue(date: LocalDate): Float {
        return ((Random.nextInt().absoluteValue % 5).toFloat() / 4)
    }
}

