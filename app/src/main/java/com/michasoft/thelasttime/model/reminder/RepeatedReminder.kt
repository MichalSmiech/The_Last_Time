package com.michasoft.thelasttime.model.reminder

class RepeatedReminder(
    id: String,
    eventId: String,
    nextTriggerMillis: Long?,
) : Reminder(
    id = id,
    eventId = eventId,
    type = Type.Repeated,
    nextTriggerMillis = nextTriggerMillis,
) {

}