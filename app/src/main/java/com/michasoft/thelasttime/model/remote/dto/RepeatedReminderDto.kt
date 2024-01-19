package com.michasoft.thelasttime.model.remote.dto

import com.michasoft.thelasttime.model.reminder.RepeatedReminder


class RepeatedReminderDto(
    var eventId: String? = null,
    var periodText: String? = null
) {
    constructor(repeatedReminder: RepeatedReminder) : this(
        repeatedReminder.eventId,
        repeatedReminder.periodText
    )

    fun toModel(id: String) = RepeatedReminder(id, eventId!!, periodText!!)
}