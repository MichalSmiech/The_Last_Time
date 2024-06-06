package com.michasoft.thelasttime.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.michasoft.thelasttime.R
import com.michasoft.thelasttime.eventDetails.EventDetailsActivity
import com.michasoft.thelasttime.model.reminder.Reminder
import com.michasoft.thelasttime.repo.EventRepository
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

class CreateReminderNotificationUseCase @Inject constructor(
    private val context: Context,
    private val eventRepository: EventRepository
) {
    private val channelId = NotificationChannels.reminderChannelData.id

    suspend fun invoke(reminder: Reminder, notificationId: Int): Notification? {
        val event = eventRepository.getEvent(eventId = reminder.eventId)
        if (event == null) {
            Timber.w("Event is not exists while CreateReminderNotificationUseCase")
            return null
        }
        val addEventInstancePendingIntent =
            Intent(context, ReminderNotificationActionBroadcastReceiver::class.java).let { intent ->
                intent.putExtra(ReminderNotificationActionBroadcastReceiver.EVENT_ID, reminder.eventId)
                intent.putExtra(ReminderNotificationActionBroadcastReceiver.NOTIFICATION_ID, notificationId)
                intent.setData(Uri.parse(Random.nextInt().toString()))
                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            }
        val startEventDetailsPendingIntent = EventDetailsActivity.getLaunchIntent(context, event.id).let { intent ->
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.setData(Uri.parse(Random.nextInt().toString()))
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }
        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_reminder)
            .setContentTitle(event.name)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(R.drawable.outline_add, "add", addEventInstancePendingIntent)
            .setContentIntent(startEventDetailsPendingIntent)
            .setAutoCancel(true)
            .build()
    }
}