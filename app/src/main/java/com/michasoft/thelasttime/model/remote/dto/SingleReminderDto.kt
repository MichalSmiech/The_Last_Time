package com.michasoft.thelasttime.model.remote.dto

import com.michasoft.thelasttime.model.reminder.SingleReminder
import org.joda.time.DateTime
import java.util.Date

class SingleReminderDto(
    var eventId: String? = null,
    var dateTime: Date? = null
) {
    constructor(singleReminder: SingleReminder) : this(
        singleReminder.eventId,
        singleReminder.dateTime.toDate()
    )

    fun toModel(id: String) = SingleReminder(id, eventId!!, DateTime(dateTime!!))
}