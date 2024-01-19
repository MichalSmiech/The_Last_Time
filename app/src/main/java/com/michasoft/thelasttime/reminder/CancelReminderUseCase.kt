package com.michasoft.thelasttime.reminder

import android.app.AlarmManager
import android.content.Context
import com.michasoft.thelasttime.dataSource.RoomReminderSource
import com.michasoft.thelasttime.model.reminder.Reminder
import com.michasoft.thelasttime.model.reminder.RepeatedReminder
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Named

class CancelReminderUseCase @Inject constructor(
    private val context: Context,
    @Named("reminderChanged") private val remindersChanged: MutableSharedFlow<Unit>,
    private val localReminderSource: RoomReminderSource
) {
    suspend fun execute(reminder: Reminder) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmPendingIntent = reminder.createAlarmPendingIntent(context)
        alarmManager.cancel(alarmPendingIntent)
        if (reminder is RepeatedReminder) {
            localReminderSource.updateRepeatedReminderLabel(reminder.id, reminder.createLabel(null))
            remindersChanged.emit(Unit)
        }
    }
}