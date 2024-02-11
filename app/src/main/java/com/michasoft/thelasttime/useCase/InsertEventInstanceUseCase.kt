package com.michasoft.thelasttime.useCase

import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.reminder.RepeatedReminder
import com.michasoft.thelasttime.model.reminder.SingleReminder
import com.michasoft.thelasttime.reminder.CancelReminderUseCase
import com.michasoft.thelasttime.reminder.ScheduleReminderUseCase
import com.michasoft.thelasttime.repo.EventRepository
import com.michasoft.thelasttime.repo.ReminderRepository
import javax.inject.Inject

class InsertEventInstanceUseCase @Inject constructor(
    private val eventRepository: EventRepository,
    private val reminderRepository: ReminderRepository,
    private val scheduleReminderUseCase: ScheduleReminderUseCase,
    private val cancelReminderUseCase: CancelReminderUseCase,
) {
    suspend fun execute(instance: EventInstance) {
        eventRepository.insertEventInstance(instance)
        val reminders = reminderRepository.getEventReminders(instance.eventId)
        reminders.filterIsInstance<SingleReminder>().forEach { reminder ->
            if (reminder.isShownOrSkipped) {
                reminderRepository.deleteReminder(reminder)
            }
        }
        reminders.filterIsInstance<RepeatedReminder>().forEach { reminder ->
            cancelReminderUseCase.execute(reminder)
            scheduleReminderUseCase.execute(reminder)
        }
    }
}