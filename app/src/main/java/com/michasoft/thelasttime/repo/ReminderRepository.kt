package com.michasoft.thelasttime.repo

import com.michasoft.thelasttime.dataSource.RoomReminderSource
import com.michasoft.thelasttime.di.UserSessionScope
import com.michasoft.thelasttime.model.reminder.Reminder
import javax.inject.Inject

@UserSessionScope
class ReminderRepository @Inject constructor(
    private val roomReminderSource: RoomReminderSource
) {
    suspend fun getReminder(id: String): Reminder? {
        return roomReminderSource.getReminder(id)
    }

    suspend fun getEventReminders(eventId: String): List<Reminder> {
        return roomReminderSource.getEventReminders(eventId)
    }

    suspend fun deleteReminder(reminder: Reminder) {
        roomReminderSource.deleteReminder(reminder)
    }

    suspend fun insertReminder(reminder: Reminder) {
        return roomReminderSource.insertReminder(reminder)
    }
}