package com.michasoft.thelasttime.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.michasoft.thelasttime.model.reminder.RepeatedReminder

@Entity(tableName = RepeatedReminderEntity.TABLE_NAME)
class RepeatedReminderEntity(
    @PrimaryKey
    var id: String,
    val eventId: String,
    val nextTriggerMillis: Long?
) {
    constructor(reminder: RepeatedReminder) : this(
        reminder.id,
        reminder.eventId,
        reminder.nextTriggerMillis,
    )

    fun toModel() = RepeatedReminder(id, eventId, nextTriggerMillis)

    companion object {
        const val TABLE_NAME = "RepeatedReminders"
    }
}