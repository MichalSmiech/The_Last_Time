package com.michasoft.thelasttime.repo

import com.michasoft.thelasttime.dataSource.RoomReminderSource
import com.michasoft.thelasttime.di.UserSessionScope
import com.michasoft.thelasttime.model.reminder.Reminder
import com.michasoft.thelasttime.model.reminder.RepeatedReminder
import com.michasoft.thelasttime.model.reminder.SingleReminder
import com.michasoft.thelasttime.reminder.CancelReminderUseCase
import com.michasoft.thelasttime.reminder.ScheduleReminderUseCase
import com.michasoft.thelasttime.util.IdGenerator
import com.michasoft.thelasttime.util.notify
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Named

@UserSessionScope
class ReminderRepository @Inject constructor(
    private val roomReminderSource: RoomReminderSource,
    private val scheduleReminderUseCase: ScheduleReminderUseCase,
    private val cancelReminderUseCase: CancelReminderUseCase,
    @Named("reminderChanged") val remindersChanged: MutableSharedFlow<Unit>
) {

    suspend fun getReminder(id: String): Reminder? {
        return roomReminderSource.getReminder(id)
    }

    suspend fun getEventReminder(eventId: String): Reminder? {
        return roomReminderSource.getEventReminder(eventId)
    }

    suspend fun deleteReminder(reminder: Reminder) {
        deleteReminder(reminder = reminder, notify = true)
    }

    private suspend fun deleteReminder(reminder: Reminder, notify: Boolean) {
        roomReminderSource.deleteReminder(reminder)
        if (notify) {
            remindersChanged.notify()
        }
        cancelReminderUseCase.execute(reminder)
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
            val eventReminder = getEventReminder(reminder.eventId)
            if (eventReminder != null) {
                deleteReminder(eventReminder)
            }
        }
        roomReminderSource.insertReminder(reminder)
        scheduleReminderUseCase.execute(reminder)
        if (notify) {
            remindersChanged.notify()
        }
    }

    suspend fun notifyRemindersChanged() {
        remindersChanged.notify()
    }

    suspend fun updateReminder(reminder: Reminder) {
        val oldReminder = getReminder(reminder.id)
        if (oldReminder != null) {
            deleteReminder(reminder = reminder, notify = false)
        }
        val newReminder: Reminder = when (reminder) {
            is SingleReminder -> SingleReminder(
                IdGenerator.newId(),
                reminder.eventId,
                reminder.dateTime,
                reminder.label,
            )

            is RepeatedReminder -> RepeatedReminder(
                IdGenerator.newId(),
                reminder.eventId,
                reminder.periodText,
                reminder.label,
            )

            else -> throw Exception()
        }
        insertReminder(newReminder)
    }
}