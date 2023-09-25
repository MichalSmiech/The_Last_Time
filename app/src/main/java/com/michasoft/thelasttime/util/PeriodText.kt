package com.michasoft.thelasttime.util

import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.format.PeriodFormatter
import org.joda.time.format.PeriodFormatterBuilder

/**
 * Created by mśmiech on 25.09.2023.
 */

fun DateTime.periodText(lastTwo: Boolean): String {
    if (!lastTwo) {
        return this.periodText()
    }
    val periodText = this.periodText()
    if (periodText.isEmpty()) {
        return periodText
    }
    if (periodText.contains(", ").not()) {
        return periodText
    }
    val endText = periodText.split(" ").last()
    val split = periodText.substring(0, periodText.length - endText.length - 1).split(", ")
    if (split.size == 1) {
        return periodText
    }
    val first = split[0]
    val secondType = when {
        first.contains("year") -> "month"
        first.contains("month") -> "day"
        first.contains("day") -> "hour"
        first.contains("hour") -> "minute"
        else -> ""
    }
    val second = split[1]
    return if (second.contains(secondType)) {
        "$first, $second"
    } else {
        first
    }.plus(" $endText")
}

fun DateTime.periodText(): String {
    var periodText = if (this.isBeforeNow) {
        val period = Period(this, DateTime.now())
        period.toString(periodFormatter).ifBlank { "moments" } + " ago"
    } else {
        val period = Period(DateTime.now(), this)
        period.toString(periodFormatter).ifBlank { "moments" } + " until"
    }
    if (periodText.contains("week")) {
        periodText = changeWeeksToDays(periodText)
    }
    return periodText
}

private fun changeWeeksToDays(periodText1: String): String {
    var periodText = periodText1
    var weeksMatchResult = """, (\d+) (weeks|week)""".toRegex().find(periodText)
    if (weeksMatchResult != null) {
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
    } else {
        weeksMatchResult = """(\d+) (weeks|week)""".toRegex().find(periodText)!!
        if (periodText.contains("day")) {
            periodText = periodText.removeRange(weeksMatchResult.range)
            val daysMatchResult = """, (\d+) (days|day)""".toRegex().find(periodText)!!
            val days =
                weeksMatchResult.groups[1]!!.value.toInt() * 7 + daysMatchResult.groups[1]!!.value.toInt()
            periodText = periodText.replace(daysMatchResult.value, "$days days")
        } else {
            val days = weeksMatchResult.groups[1]!!.value.toInt() * 7
            periodText = periodText.replace(weeksMatchResult.value, "$days days")
        }
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