package com.michasoft.thelasttime.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.michasoft.thelasttime.LastTimeApplication
import com.michasoft.thelasttime.dataSource.RoomReminderSource
import com.michasoft.thelasttime.model.reminder.RepeatedReminder
import com.michasoft.thelasttime.model.reminder.SingleReminder
import com.michasoft.thelasttime.notification.CreateNotificationChannelUseCase
import com.michasoft.thelasttime.notification.CreateReminderNotificationUseCase
import com.michasoft.thelasttime.notification.NotificationChannels
import com.michasoft.thelasttime.notification.ShowNotificationUseCase
import com.michasoft.thelasttime.repo.EventRepository
import com.michasoft.thelasttime.repo.ReminderRepository
import com.michasoft.thelasttime.util.notify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
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

    @Inject
    lateinit var reminderRepository: ReminderRepository

    @Inject
    lateinit var localReminderSource: RoomReminderSource

    @Inject
    @Named("reminderChanged")
    lateinit var remindersChanged: MutableSharedFlow<Unit>

    override fun onReceive(context: Context?, intent: Intent?) {
        val reminderId = intent?.getStringExtra(REMINDER_ID) ?: return
        (context?.applicationContext as LastTimeApplication?)?.userSessionComponent?.inject(this)
        CoroutineScope(Dispatchers.Main).launch {
            val reminder = reminderRepository.getReminder(reminderId) ?: return@launch

            val channelData = NotificationChannels.reminderChannelData
            createNotificationChannelUseCase.invoke(channelData)

            val notificationId = Random.nextInt() //TODO save in reminder?
            val notification = createReminderNotificationUseCase.invoke(reminder) ?: return@launch
            showNotificationUseCase.invoke(notification, notificationId)
            if (reminder is SingleReminder) {
                reminderRepository.deleteReminder(reminder)
            }
            if (reminder is RepeatedReminder) {
                localReminderSource.updateRepeatedReminderLabel(reminder.id, reminder.createLabel(null))
                remindersChanged.notify()
            }
        }
    }

    companion object {
        const val REMINDER_ID = "reminderId"
    }
}