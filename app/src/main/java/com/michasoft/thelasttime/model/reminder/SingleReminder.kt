package com.michasoft.thelasttime.model.reminder

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

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
        private val labelDatetimeFormatter = DateTimeFormat.forPattern("dd MMM, HH:mm")
        fun createLabel(dateTime: DateTime): String {
            return dateTime.toString(labelDatetimeFormatter)
        }
    }
}