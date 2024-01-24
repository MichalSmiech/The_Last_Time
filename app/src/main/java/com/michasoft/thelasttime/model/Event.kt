package com.michasoft.thelasttime.model

import com.michasoft.thelasttime.model.reminder.Reminder
import org.joda.time.DateTime

/**
 * Created by mśmiech on 02.05.2021.
 */
data class Event(
    var id: String,
    var name: String,
    val createTimestamp: DateTime,
    val eventInstanceSchema: EventInstanceSchema
) {
    var lastInstanceTimestamp: DateTime? = null
    var labels: List<Label> = emptyList()
    var reminders: List<Reminder> = emptyList()
}