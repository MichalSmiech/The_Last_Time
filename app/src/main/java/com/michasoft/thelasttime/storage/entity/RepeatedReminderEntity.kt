package com.michasoft.thelasttime.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.michasoft.thelasttime.model.reminder.RepeatedReminder
import org.joda.time.DateTime

@Entity(tableName = RepeatedReminderEntity.TABLE_NAME)
class RepeatedReminderEntity(
    @PrimaryKey
    var id: String,
    val eventId: String,
    val triggerDateTime: DateTime?,
    val periodText: String,
) {
    constructor(reminder: RepeatedReminder) : this(
        reminder.id,
        reminder.eventId,
        reminder.triggerDateTime,
        reminder.periodText,
    )

    fun toModel() = RepeatedReminder(
        id = id,
        eventId = eventId,
        triggerDateTime = triggerDateTime,
        periodText = periodText,
    )

    companion object {
        const val TABLE_NAME = "RepeatedReminders"
    }
}