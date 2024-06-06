package com.michasoft.thelasttime.model.dto

import com.michasoft.thelasttime.model.reminder.SingleReminder
import org.joda.time.DateTime
import java.util.Date

class SingleReminderDto(
    var eventId: String? = null,
    var dateTime: Date? = null,
    var reshowEnabled: Boolean? = null,
) {
    constructor(singleReminder: SingleReminder) : this(
        singleReminder.eventId,
        singleReminder.dateTime.toDate(),
        singleReminder.reshowEnabled
    )

    fun toModel(id: String) = SingleReminder(
        id = id,
        eventId = eventId!!,
        dateTime = DateTime(dateTime!!),
        reshowEnabled = reshowEnabled ?: true
    )
}