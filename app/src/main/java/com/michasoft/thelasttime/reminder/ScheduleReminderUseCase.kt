package com.michasoft.thelasttime.reminder

import android.app.AlarmManager
import android.content.Context
import android.os.SystemClock
import com.michasoft.thelasttime.dataSource.ILocalEventSource
import com.michasoft.thelasttime.dataSource.RoomReminderSource
import com.michasoft.thelasttime.model.reminder.Reminder
import com.michasoft.thelasttime.model.reminder.RepeatedReminder
import com.michasoft.thelasttime.model.reminder.SingleReminder
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
        val nextTriggerMillis = when (reminder) {
            is SingleReminder -> reminder.calcNextTriggerMillis()
            is RepeatedReminder -> {
                val lastInstanceTimestamp =
                    localEventSource.getLastInstanceTimestamp(reminder.eventId)
                if (lastInstanceTimestamp == null) {
                    null
                } else {
                    reminder.calcNextTriggerMillis(lastInstanceTimestamp)
                }
            }

            else -> null
        }
        if (nextTriggerMillis != null) {
            alarmManager.set(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + nextTriggerMillis,
                alarmPendingIntent
            )
            Timber.d("execute nextTriggerMillis=$nextTriggerMillis")
        } else {
            Timber.e("reminder nextTriggerMillis is null while ScheduleReminderUseCase, reminderId=${reminder.id}")
        }
        if (reminder is RepeatedReminder) {
            val lastInstanceTimestamp = localEventSource.getLastInstanceTimestamp(reminder.eventId)
            localReminderSource.updateRepeatedReminderLabel(reminder.id, reminder.createLabel(lastInstanceTimestamp))
            remindersChanged.emit(Unit)
        }
    }
}