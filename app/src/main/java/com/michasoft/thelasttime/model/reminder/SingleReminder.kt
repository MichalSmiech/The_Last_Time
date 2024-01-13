package com.michasoft.thelasttime.model.reminder

import org.joda.time.DateTime

class SingleReminder(
    id: String,
    eventId: String,
    val dateTime: DateTime,
    nextTriggerMillis: Long? = null,
) : Reminder(
    id = id,
    eventId = eventId,
    type = Type.Single,
    nextTriggerMillis = nextTriggerMillis,
) {

}