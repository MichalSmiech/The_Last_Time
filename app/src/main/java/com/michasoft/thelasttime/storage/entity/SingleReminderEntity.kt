package com.michasoft.thelasttime.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.michasoft.thelasttime.model.reminder.SingleReminder

@Entity(tableName = SingleReminderEntity.TABLE_NAME)
class SingleReminderEntity(
    @PrimaryKey
    var id: String,
    val eventId: String,
    val nextTriggerMillis: Long?
) {
    constructor(reminder: SingleReminder) : this(
        reminder.id,
        reminder.eventId,
        reminder.nextTriggerMillis,
    )

    fun toModel() = SingleReminder(id, eventId, nextTriggerMillis)

    companion object {
        const val TABLE_NAME = "SingleReminders"
    }
}