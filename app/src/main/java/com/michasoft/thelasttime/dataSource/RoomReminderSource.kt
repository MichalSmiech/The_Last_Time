package com.michasoft.thelasttime.dataSource

import com.michasoft.thelasttime.di.UserSessionScope
import com.michasoft.thelasttime.model.reminder.Reminder
import com.michasoft.thelasttime.model.reminder.RepeatedReminder
import com.michasoft.thelasttime.model.reminder.SingleReminder
import com.michasoft.thelasttime.storage.dao.EventDao
import com.michasoft.thelasttime.storage.dao.ReminderDao
import com.michasoft.thelasttime.storage.entity.RepeatedReminderEntity
import com.michasoft.thelasttime.storage.entity.SingleReminderEntity
import javax.inject.Inject

@UserSessionScope
class RoomReminderSource @Inject constructor(
    private val reminderDao: ReminderDao,
    private val eventDao: EventDao
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
            .plus(reminderDao.getEventRepeatedReminders(eventId).map { it.toModel().applyLabel() })
    }

    suspend fun getReminder(id: String): Reminder? =
        reminderDao.getSingleReminder(id)?.toModel()
            ?: reminderDao.getRepeatedReminder(id)?.toModel()?.applyLabel()

    private suspend fun RepeatedReminder.applyLabel(): RepeatedReminder {
        return this.apply {
            label = RepeatedReminder.createLabel(
                eventDao.getLastInstanceTimestamp(eventId),
                periodText
            )
        }
    }

    suspend fun updateReminder(reminder: Reminder) {
        when (reminder) {
            is SingleReminder -> reminderDao.updateSingleReminder(
                reminder.id,
                reminder.dateTime,
                reminder.label
            )

            is RepeatedReminder -> reminderDao.updateRepeatedReminder(
                reminder.id,
                reminder.periodText
            )
        }
    }
}