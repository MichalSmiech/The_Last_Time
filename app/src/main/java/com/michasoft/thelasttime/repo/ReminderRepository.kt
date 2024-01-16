package com.michasoft.thelasttime.repo

import com.michasoft.thelasttime.dataSource.RoomReminderSource
import com.michasoft.thelasttime.di.UserSessionScope
import com.michasoft.thelasttime.model.reminder.Reminder
import com.michasoft.thelasttime.model.reminder.RepeatedReminder
import com.michasoft.thelasttime.model.reminder.SingleReminder
import com.michasoft.thelasttime.reminder.CancelReminderUseCase
import com.michasoft.thelasttime.reminder.ScheduleReminderUseCase
import com.michasoft.thelasttime.util.IdGenerator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

@UserSessionScope
class ReminderRepository @Inject constructor(
    private val roomReminderSource: RoomReminderSource,
    private val scheduleReminderUseCase: ScheduleReminderUseCase,
    private val cancelReminderUseCase: CancelReminderUseCase
) {
    private val _remindersChanged: MutableSharedFlow<Unit> = MutableSharedFlow()
    val remindersChanged: SharedFlow<Unit> = _remindersChanged

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
            _remindersChanged.emit(Unit)
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
        }
        roomReminderSource.insertReminder(reminder)
        if (notify) {
            _remindersChanged.emit(Unit)
        }
        scheduleReminderUseCase.execute(reminder)
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
                reminder.nextTriggerMillis
            )

            is RepeatedReminder -> RepeatedReminder(
                IdGenerator.newId(),
                reminder.eventId,
                reminder.periodText,
                reminder.label,
                reminder.nextTriggerMillis
            )

            else -> throw Exception()
        }
        insertReminder(newReminder)
    }
}