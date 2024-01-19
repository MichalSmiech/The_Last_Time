package com.michasoft.thelasttime.model.reminder

import org.joda.time.DateTime

class SingleReminder(
    id: String,
    eventId: String,
    val dateTime: DateTime,
    label: String,
) : Reminder(
    id = id,
    eventId = eventId,
    type = Type.Single,
    label = label,
) {
    constructor(id: String, eventId: String, dateTime: DateTime) : this(
        id,
        eventId,
        dateTime,
        createLabel(dateTime)
    )

    fun calcNextTriggerMillis(): Long {
        return dateTime.millis - DateTime.now().millis
    }

    fun getNextTrigger(): DateTime {
        return dateTime
    }
}