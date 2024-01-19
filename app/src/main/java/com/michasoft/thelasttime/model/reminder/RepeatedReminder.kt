package com.michasoft.thelasttime.model.reminder

import org.joda.time.DateTime

class RepeatedReminder(
    id: String,
    eventId: String,
    val periodText: String,
    label: String,
) : Reminder(
    id = id,
    eventId = eventId,
    type = Type.Repeated,
    label = label,
) {
    constructor(id: String, eventId: String, periodText: String) : this(
        id = id,
        eventId = eventId,
        periodText = periodText,
        label = periodText
    )

    fun createLabel(nextTrigger: DateTime?): String {
        if (nextTrigger == null) {
            return periodText
        }
        return Reminder.createLabel(nextTrigger)
    }


    fun getNextTrigger(lastEventInstanceDateTime: DateTime): DateTime? {
        val nextTrigger = getNextTrigger(periodText, lastEventInstanceDateTime)
        if (nextTrigger.isBeforeNow) {
            return null
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