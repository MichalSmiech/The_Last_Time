package com.michasoft.thelasttime.model.reminder

import org.joda.time.DateTime

class RepeatedReminder(
    id: String,
    eventId: String,
    val periodText: String,
    label: String,
    nextTriggerMillis: Long? = null,
) : Reminder(
    id = id,
    eventId = eventId,
    type = Type.Repeated,
    label = label,
    nextTriggerMillis = nextTriggerMillis,
) {
    constructor(id: String, eventId: String, periodText: String) : this(
        id = id,
        eventId = eventId,
        periodText = periodText,
        label = periodText //TODO ustawić następna datę
    )

    companion object {
        fun createLabel(lastEventInstanceDateTime: DateTime?, periodText: String): String {
            if (lastEventInstanceDateTime == null) {
                return periodText
            }
            val nextTriggerDateTime = getNextTrigger(periodText, lastEventInstanceDateTime)
            return Reminder.createLabel(nextTriggerDateTime)
        }

        fun getNextTrigger(periodText: String, startDateTime: DateTime = DateTime.now()): DateTime {
            if (periodText.isBlank()) {
                throw IllegalArgumentException()
            }
            var nextTrigger = startDateTime
            val usedTypes = mutableSetOf<String>()
            periodText.trim().split(regex = "\\s+".toRegex()).forEach {
                val value = "\\d+".toRegex().find(it)!!.value.toInt()
                val type = "y|m|w|d|h|min".toRegex().find(it)!!.value
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