package com.michasoft.thelasttime.model.dto

import com.michasoft.thelasttime.model.TimeRange
import com.michasoft.thelasttime.model.reminder.RepeatedReminder


class RepeatedReminderDto(
    var eventId: String? = null,
    var periodText: String? = null,
    var timeRangeString: String? = null,
    var reshowEnabled: Boolean? = null,
) {
    constructor(repeatedReminder: RepeatedReminder) : this(
        repeatedReminder.eventId,
        repeatedReminder.periodText,
        repeatedReminder.timeRange?.serialize(),
        repeatedReminder.reshowEnabled,
    )

    fun toModel(id: String) = RepeatedReminder(
        id = id,
        eventId = eventId!!,
        periodText = periodText!!,
        timeRange = TimeRange.deserialize(timeRangeString),
        reshowEnabled = reshowEnabled ?: true
    )
}