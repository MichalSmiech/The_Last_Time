package com.michasoft.thelasttime.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.michasoft.thelasttime.model.reminder.SingleReminder
import org.joda.time.DateTime

@Entity(tableName = SingleReminderEntity.TABLE_NAME)
class SingleReminderEntity(
    @PrimaryKey
    var id: String,
    val eventId: String,
    val dateTime: DateTime,
    val label: String,
) {
    constructor(reminder: SingleReminder) : this(
        reminder.id,
        reminder.eventId,
        reminder.dateTime,
        reminder.label,
    )

    fun toModel() = SingleReminder(id, eventId, dateTime, label)

    companion object {
        const val TABLE_NAME = "SingleReminders"
    }
}