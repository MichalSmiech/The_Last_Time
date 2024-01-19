package com.michasoft.thelasttime.notification

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import com.michasoft.thelasttime.R
import com.michasoft.thelasttime.model.reminder.Reminder
import com.michasoft.thelasttime.repo.EventRepository
import timber.log.Timber
import javax.inject.Inject

class CreateReminderNotificationUseCase @Inject constructor(
    private val context: Context,
    private val eventRepository: EventRepository
) {
    private val channelId = NotificationChannels.reminderChannelData.id

    suspend fun invoke(reminder: Reminder): Notification? {
        val event = eventRepository.getEvent(eventId = reminder.eventId)
        if (event == null) {
            Timber.w("Event is not exists while CreateReminderNotificationUseCase")
            return null
        }
        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.baseline_schedule_24)
            .setContentTitle("Reminder")
            .setContentText(event.name)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }
}