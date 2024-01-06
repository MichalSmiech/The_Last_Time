package com.michasoft.thelasttime.notification

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import com.michasoft.thelasttime.R
import com.michasoft.thelasttime.model.Reminder
import javax.inject.Inject

class CreateReminderNotificationUseCase @Inject constructor(
    private val context: Context
) {
    private val channelId = NotificationChannels.reminderChannelData.id

    operator fun invoke(reminder: Reminder): Notification {
        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.baseline_schedule_24)
            .setContentTitle("Reminder")
            .setContentText(reminder.event.name)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }
}