package com.michasoft.thelasttime.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.michasoft.thelasttime.model.TimeRange
import com.michasoft.thelasttime.model.reminder.RepeatedReminder
import org.joda.time.DateTime

@Entity(tableName = RepeatedReminderEntity.TABLE_NAME)
class RepeatedReminderEntity(
    @PrimaryKey
    var id: String,
    val eventId: String,
    val triggerDateTime: DateTime?,
    val periodText: String,
    val timeRange: TimeRange?,
    val reshowEnabled: Boolean,
) {
    constructor(reminder: RepeatedReminder) : this(
        reminder.id,
        reminder.eventId,
        reminder.triggerDateTime,
        reminder.periodText,
        reminder.timeRange,
        reminder.reshowEnabled,
    )

    fun toModel() = RepeatedReminder(
        id = id,
        eventId = eventId,
        triggerDateTime = triggerDateTime,
        periodText = periodText,
        timeRange = timeRange,
        reshowEnabled = reshowEnabled,
    )

    companion object {
        const val TABLE_NAME = "RepeatedReminders"
    }
}