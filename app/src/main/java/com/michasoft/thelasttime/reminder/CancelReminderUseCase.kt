package com.michasoft.thelasttime.reminder

import android.app.AlarmManager
import android.content.Context
import com.michasoft.thelasttime.dataSource.RoomReminderSource
import com.michasoft.thelasttime.model.reminder.Reminder
import com.michasoft.thelasttime.notification.CancelReminderNotificationsUseCase
import javax.inject.Inject

class CancelReminderUseCase @Inject constructor(
    private val context: Context,
    private val localReminderSource: RoomReminderSource,
    private val cancelReminderNotificationsUseCase: CancelReminderNotificationsUseCase
) {
    suspend fun execute(reminder: Reminder) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmPendingIntent = reminder.createAlarmPendingIntent(context)
        alarmManager.cancel(alarmPendingIntent)
        localReminderSource.updateReminderTriggerDateTime(reminder, triggerDateTime = null)
        cancelReminderNotificationsUseCase.invoke(reminder.id)
    }
}