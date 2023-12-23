package com.michasoft.thelasttime.notification

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import com.michasoft.thelasttime.model.Reminder

class CreateReminderNotificationUseCase(
    private val context: Context
) {
    private val channelId = NotificationChannels.reminderChannelData.id

    operator fun invoke(reminder: Reminder): Notification {
        return NotificationCompat.Builder(context, channelId)
//            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle(reminder.text)
//            .setContentText(textContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }
}