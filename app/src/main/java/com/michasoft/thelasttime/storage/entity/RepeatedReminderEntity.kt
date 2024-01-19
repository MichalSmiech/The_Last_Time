package com.michasoft.thelasttime.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.michasoft.thelasttime.model.reminder.RepeatedReminder

@Entity(tableName = RepeatedReminderEntity.TABLE_NAME)
class RepeatedReminderEntity(
    @PrimaryKey
    var id: String,
    val eventId: String,
    val periodText: String,
    val label: String
) {
    constructor(reminder: RepeatedReminder) : this(
        reminder.id,
        reminder.eventId,
        reminder.periodText,
        reminder.label
    )

    fun toModel() = RepeatedReminder(
        id = id,
        eventId = eventId,
        periodText = periodText,
        label = label,
    )

    companion object {
        const val TABLE_NAME = "RepeatedReminders"
    }
}