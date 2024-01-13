package com.michasoft.thelasttime.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.SystemClock
import com.michasoft.thelasttime.model.reminder.Reminder
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

class ScheduleReminderUseCase @Inject constructor(private val context: Context) {
    fun execute(reminder: Reminder) {
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, ShowReminderReceiver::class.java).let { intent ->
            intent.putExtra(ShowReminderReceiver.REMINDER_ID, reminder.id)
            intent.setData(Uri.parse(Random.nextInt().toString()))
            PendingIntent.getBroadcast(context, 0, intent, FLAG_IMMUTABLE)
        }
        if (reminder.nextTriggerMillis == null) {
            Timber.e("reminder nextTriggerMillis is null while ScheduleReminderUseCase, reminderId=${reminder.id}")
            return
        }
        alarmMgr.set(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + reminder.nextTriggerMillis,
            alarmIntent
        )
    }
}