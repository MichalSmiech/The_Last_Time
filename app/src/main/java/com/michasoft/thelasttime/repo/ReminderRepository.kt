package com.michasoft.thelasttime.repo

import com.michasoft.thelasttime.dataSource.RoomReminderSource
import com.michasoft.thelasttime.di.UserSessionScope
import com.michasoft.thelasttime.model.reminder.Reminder
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

@UserSessionScope
class ReminderRepository @Inject constructor(
    private val roomReminderSource: RoomReminderSource
) {
    private val _remindersChanged: MutableSharedFlow<Unit> = MutableSharedFlow()
    val remindersChanged: SharedFlow<Unit> = _remindersChanged

    suspend fun getReminder(id: String): Reminder? {
        return roomReminderSource.getReminder(id)
    }

    suspend fun getEventReminders(eventId: String): List<Reminder> {
        return roomReminderSource.getEventReminders(eventId)
    }

    suspend fun deleteReminder(reminder: Reminder) {
        deleteReminder(reminder = reminder, notify = true)
    }

    private suspend fun deleteReminder(reminder: Reminder, notify: Boolean) {
        roomReminderSource.deleteReminder(reminder)
        if (notify) {
            _remindersChanged.emit(Unit)
        }
    }

    suspend fun deleteReminder(reminderId: String) {
        val reminder = getReminder(reminderId) ?: return
        deleteReminder(reminder)
    }

    suspend fun insertReminder(reminder: Reminder) {
        insertReminder(reminder = reminder, check = true, notify = true)
    }

    private suspend fun insertReminder(reminder: Reminder, check: Boolean, notify: Boolean) {
        if (check) {
            val reminder1 = getReminder(reminder.id)
            if (reminder1 != null) {
                deleteReminder(reminder1)
            }
        }
        roomReminderSource.insertReminder(reminder)
        if (notify) {
            _remindersChanged.emit(Unit)
        }
    }

    suspend fun updateReminder(reminder: Reminder) {
        val oldReminder = getReminder(reminder.id)
        if (oldReminder == null) {
            insertReminder(reminder = reminder, check = false, notify = true)
        } else {
            if (oldReminder.type != reminder.type) {
                deleteReminder(reminder = oldReminder, notify = false)
                insertReminder(reminder = reminder, check = false, notify = true)
            } else {
                roomReminderSource.updateReminder(reminder)
                _remindersChanged.emit(Unit)
            }
        }
    }
}