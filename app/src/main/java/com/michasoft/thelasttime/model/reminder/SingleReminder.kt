package com.michasoft.thelasttime.model.reminder

import org.joda.time.DateTime

class SingleReminder(
    id: String,
    eventId: String,
    triggerDateTime: DateTime?,
    val dateTime: DateTime,
    reshowEnabled: Boolean
) : Reminder(
    id = id,
    eventId = eventId,
    triggerDateTime = triggerDateTime,
    reshowEnabled = reshowEnabled
) {
    override val label: String
        get() = createLabel(triggerDateTime ?: dateTime)
    override val type: Type
        get() = Type.Single

    constructor(
        id: String,
        eventId: String,
        dateTime: DateTime,
        reshowEnabled: Boolean,
    ) : this(
        id = id,
        eventId = eventId,
        triggerDateTime = null,
        dateTime = dateTime,
        reshowEnabled = reshowEnabled
    )

    fun getNextTrigger(): DateTime {
        return dateTime
    }

}