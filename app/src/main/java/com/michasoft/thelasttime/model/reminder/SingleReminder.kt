package com.michasoft.thelasttime.model.reminder

import org.joda.time.DateTime

class SingleReminder(
    id: String,
    eventId: String,
    triggerDateTime: DateTime?,
    val dateTime: DateTime,
) : Reminder(
    id = id,
    eventId = eventId,
    triggerDateTime = triggerDateTime,
) {
    override val label: String
        get() = createLabel(triggerDateTime ?: dateTime)
    override val type: Type
        get() = Type.Single

    constructor(
        id: String,
        eventId: String,
        dateTime: DateTime,
    ) : this(
        id = id,
        eventId = eventId,
        triggerDateTime = null,
        dateTime = dateTime,
    )

    fun getNextTrigger(): DateTime {
        return dateTime
    }

}