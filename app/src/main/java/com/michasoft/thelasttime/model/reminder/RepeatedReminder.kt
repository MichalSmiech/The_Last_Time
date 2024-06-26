package com.michasoft.thelasttime.model.reminder

import com.michasoft.thelasttime.model.TimeRange
import org.joda.time.DateTime

class RepeatedReminder(
    id: String,
    eventId: String,
    triggerDateTime: DateTime?,
    val periodText: String,
    val timeRange: TimeRange?,
    reshowEnabled: Boolean,
) : Reminder(
    id = id,
    eventId = eventId,
    triggerDateTime = triggerDateTime,
    reshowEnabled = reshowEnabled
) {
    override val label: String
        get() = triggerDateTime?.let { createLabel(it) } ?: periodText
    override val type: Type
        get() = Type.Repeated

    constructor(
        id: String,
        eventId: String,
        periodText: String,
        timeRange: TimeRange?,
        reshowEnabled: Boolean
    ) : this(
        id = id,
        eventId = eventId,
        triggerDateTime = null,
        periodText = periodText,
        timeRange = timeRange,
        reshowEnabled = reshowEnabled
    )

    fun getNextTrigger(lastEventInstanceDateTime: DateTime): DateTime? {
        var nextTrigger = getNextTrigger(periodText, lastEventInstanceDateTime)
        if (timeRange != null) {
            if (nextTrigger.toLocalTime().isBefore(timeRange.start)) {
                nextTrigger = nextTrigger.withTime(timeRange.start)
            } else if (nextTrigger.toLocalTime().isAfter(timeRange.end)) {
                nextTrigger = nextTrigger.plusDays(1).withTime(timeRange.start)
            }
        }
        return nextTrigger
    }

    companion object {
        fun getNextTrigger(periodText: String, startDateTime: DateTime = DateTime.now()): DateTime {
            if (periodText.isBlank()) {
                throw IllegalArgumentException()
            }
            var nextTrigger = DateTime(startDateTime).withSecondOfMinute(0)
            val usedTypes = mutableSetOf<String>()
            periodText.trim().split(regex = "\\s+".toRegex()).forEach {
                val value = "\\d+".toRegex().find(it)!!.value.toInt()
                val type = "min|y|m|w|d|h".toRegex().find(it)!!.value
                if (type in usedTypes) {
                    throw IllegalArgumentException(type)
                }
                nextTrigger = when (type) {
                    "y" -> {
                        nextTrigger.plusYears(value)
                    }

                    "m" -> {
                        nextTrigger.plusMonths(value)
                    }

                    "w" -> {
                        nextTrigger.plusWeeks(value)
                    }

                    "d" -> {
                        nextTrigger.plusDays(value)
                    }

                    "h" -> {
                        nextTrigger.plusHours(value)
                    }

                    "min" -> {
                        nextTrigger.plusMinutes(value)
                    }

                    else -> {
                        throw IllegalArgumentException(type)
                    }
                }
                usedTypes.add(type)
            }
            return nextTrigger
        }
    }
}