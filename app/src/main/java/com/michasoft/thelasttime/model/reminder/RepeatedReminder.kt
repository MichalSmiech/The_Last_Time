package com.michasoft.thelasttime.model.reminder

import org.joda.time.DateTime

class RepeatedReminder(
    id: String,
    eventId: String,
    val periodText: String,
    nextTriggerMillis: Long? = null,
) : Reminder(
    id = id,
    eventId = eventId,
    type = Type.Repeated,
    nextTriggerMillis = nextTriggerMillis,
) {

    companion object {
        fun getNextTrigger(periodText: String): DateTime {
            if (periodText.isBlank()) {
                throw IllegalArgumentException()
            }
            var nextTrigger = DateTime.now()
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