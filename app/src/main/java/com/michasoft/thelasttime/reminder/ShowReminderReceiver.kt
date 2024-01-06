package com.michasoft.thelasttime.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.michasoft.thelasttime.LastTimeApplication
import com.michasoft.thelasttime.model.Reminder
import com.michasoft.thelasttime.notification.CreateNotificationChannelUseCase
import com.michasoft.thelasttime.notification.CreateReminderNotificationUseCase
import com.michasoft.thelasttime.notification.NotificationChannels
import com.michasoft.thelasttime.notification.ShowNotificationUseCase
import com.michasoft.thelasttime.repo.EventRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

class ShowReminderReceiver : BroadcastReceiver() {
    @Inject
    lateinit var eventRepository: EventRepository

    @Inject
    lateinit var createNotificationChannelUseCase: CreateNotificationChannelUseCase

    @Inject
    lateinit var showNotificationUseCase: ShowNotificationUseCase

    @Inject
    lateinit var createReminderNotificationUseCase: CreateReminderNotificationUseCase

    override fun onReceive(context: Context?, intent: Intent?) {
        val eventId = intent?.getStringExtra(EVENT_ID) ?: return
        (context?.applicationContext as LastTimeApplication?)?.userSessionComponent?.inject(this)
        CoroutineScope(Dispatchers.Main).launch {
            val event = eventRepository.getEvent(eventId) ?: return@launch
            val reminder = Reminder(event)

            val channelData = NotificationChannels.reminderChannelData
            createNotificationChannelUseCase.invoke(channelData)

            val notificationId = Random.nextInt()
            val notification = createReminderNotificationUseCase(reminder)
            showNotificationUseCase.invoke(
                notification,
                notificationId
            )
        }
    }

    companion object {
        const val EVENT_ID = "eventId"
    }
}