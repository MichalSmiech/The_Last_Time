package com.michasoft.thelasttime.model.dto

import com.michasoft.thelasttime.model.TimeRange
import com.michasoft.thelasttime.model.reminder.RepeatedReminder


class RepeatedReminderDto(
    var eventId: String? = null,
    var periodText: String? = null,
    var timeRangeString: String? = null
) {
    constructor(repeatedReminder: RepeatedReminder) : this(
        repeatedReminder.eventId,
        repeatedReminder.periodText,
        repeatedReminder.timeRange?.serialize()
    )

    fun toModel(id: String) = RepeatedReminder(id, eventId!!, periodText!!, TimeRange.deserialize(timeRangeString))
}