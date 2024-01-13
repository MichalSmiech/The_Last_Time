package com.michasoft.thelasttime.model.reminder

class SingleReminder(
    id: String,
    eventId: String,
    nextTriggerMillis: Long?,
) : Reminder(
    id = id,
    eventId = eventId,
    type = Type.Single,
    nextTriggerMillis = nextTriggerMillis,
) {

}