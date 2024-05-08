package com.michasoft.thelasttime.dataSource

import com.michasoft.thelasttime.di.UserSessionScope
import com.michasoft.thelasttime.model.reminder.Reminder
import com.michasoft.thelasttime.model.reminder.RepeatedReminder
import com.michasoft.thelasttime.model.reminder.SingleReminder
import com.michasoft.thelasttime.storage.dao.ReminderDao
import com.michasoft.thelasttime.storage.entity.RepeatedReminderEntity
import com.michasoft.thelasttime.storage.entity.SingleReminderEntity
import org.joda.time.DateTime
import javax.inject.Inject

@UserSessionScope
class RoomReminderSource @Inject constructor(
    private val reminderDao: ReminderDao,
) {
    suspend fun insertReminder(reminder: Reminder) {
        when (reminder) {
            is SingleReminder -> reminderDao.insertSingleReminder(SingleReminderEntity(reminder))
            is RepeatedReminder -> reminderDao.insertRepeatedReminder(
                RepeatedReminderEntity(
                    reminder
                )
            )
        }
    }

    suspend fun deleteReminder(reminder: Reminder) {
        when (reminder) {
            is SingleReminder -> reminderDao.deleteSingleReminder(reminder.id)
            is RepeatedReminder -> reminderDao.deleteRepeatedReminder(reminder.id)
        }
    }

    suspend fun getEventReminders(eventId: String): List<Reminder> {
        return reminderDao.getEventSingleReminders(eventId).map { it.toModel() }
            .plus(reminderDao.getEventRepeatedReminders(eventId).map { it.toModel() })
    }

    suspend fun getReminder(id: String): Reminder? =
        reminderDao.getSingleReminder(id)?.toModel()
            ?: reminderDao.getRepeatedReminder(id)?.toModel()

    suspend fun getReminders(): List<Reminder> {
        val list = mutableListOf<Reminder>()
        list.addAll(reminderDao.getSingleReminders().map { it.toModel() })
        list.addAll(reminderDao.getRepeatedReminders().map { it.toModel() })
        return list
    }

    suspend fun updateReminderTriggerDateTime(reminder: Reminder, triggerDateTime: DateTime?) {
        when (reminder) {
            is SingleReminder -> reminderDao.updateSingleReminderTriggerDateTime(reminder.id, triggerDateTime)
            is RepeatedReminder -> reminderDao.updateRepeatedReminderTriggerDateTime(reminder.id, triggerDateTime)
        }
    }

    suspend fun clear() {
        reminderDao.deleteAllSingleReminders()
        reminderDao.deleteAllRepeatedReminders()
    }

    suspend fun hasReminders(): Boolean {
        if (reminderDao.hasRepeatedReminders() == 1) {
            return true
        }
        return reminderDao.hasSingleReminders() == 1
    }
}