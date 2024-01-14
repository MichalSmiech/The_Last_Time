package com.michasoft.thelasttime.model.reminder

import org.joda.time.DateTime

class SingleReminder(
    id: String,
    eventId: String,
    val dateTime: DateTime,
    label: String,
    nextTriggerMillis: Long? = null,
) : Reminder(
    id = id,
    eventId = eventId,
    type = Type.Single,
    label = label,
    nextTriggerMillis = nextTriggerMillis,
) {
    constructor(id: String, eventId: String, dateTime: DateTime) : this(
        id,
        eventId,
        dateTime,
        createLabel(dateTime)
    )


    companion object {
        fun createLabel(dateTime: DateTime): String {
            return Reminder.createLabel(dateTime)
        }
    }

    fun calcNextTriggerMillis(): Long {
        return dateTime.millis - DateTime.now().millis
    }
}