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
    val triggerDateTime: DateTime?,
    val dateTime: DateTime,
    val reshowEnabled: Boolean
) {
    constructor(reminder: SingleReminder) : this(
        reminder.id,
        reminder.eventId,
        reminder.triggerDateTime,
        reminder.dateTime,
        reminder.reshowEnabled,
    )

    fun toModel() = SingleReminder(id, eventId, triggerDateTime, dateTime, reshowEnabled)

    companion object {
        const val TABLE_NAME = "SingleReminders"
    }
}