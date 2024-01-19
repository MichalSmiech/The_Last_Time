package com.michasoft.thelasttime.reminder

import android.app.AlarmManager
import android.content.Context
import com.michasoft.thelasttime.dataSource.ILocalEventSource
import com.michasoft.thelasttime.dataSource.RoomReminderSource
import com.michasoft.thelasttime.model.reminder.Reminder
import com.michasoft.thelasttime.model.reminder.RepeatedReminder
import com.michasoft.thelasttime.model.reminder.SingleReminder
import com.michasoft.thelasttime.util.notify
import kotlinx.coroutines.flow.MutableSharedFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class ScheduleReminderUseCase @Inject constructor(
    private val context: Context,
    private val localEventSource: ILocalEventSource,
    private val localReminderSource: RoomReminderSource,
    @Named("reminderChanged") private val remindersChanged: MutableSharedFlow<Unit>
) {
    suspend fun execute(reminder: Reminder) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmPendingIntent = reminder.createAlarmPendingIntent(context)
        val nextTrigger = when (reminder) {
            is SingleReminder -> reminder.getNextTrigger()
            is RepeatedReminder -> {
                val lastInstanceTimestamp =
                    localEventSource.getLastInstanceTimestamp(reminder.eventId)
                if (lastInstanceTimestamp == null) {
                    null
                } else {
                    reminder.getNextTrigger(lastInstanceTimestamp)
                }
            }

            else -> null
        }
        if (nextTrigger != null) {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                nextTrigger.millis,
                alarmPendingIntent
            )
            Timber.d("execute nextTrigger=$nextTrigger")
        } else {
            Timber.d("reminder nextTrigger is null while ScheduleReminderUseCase, reminderId=${reminder.id}")
        }
        if (reminder is RepeatedReminder) {
            localReminderSource.updateRepeatedReminderLabel(reminder.id, reminder.createLabel(nextTrigger))
            remindersChanged.notify()
        }
    }
}