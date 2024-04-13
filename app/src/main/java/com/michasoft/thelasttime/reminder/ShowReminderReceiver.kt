package com.michasoft.thelasttime.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.michasoft.thelasttime.LastTimeApplication
import com.michasoft.thelasttime.dataSource.RoomNotificationSource
import com.michasoft.thelasttime.dataSource.RoomReminderSource
import com.michasoft.thelasttime.notification.CreateNotificationChannelUseCase
import com.michasoft.thelasttime.notification.CreateReminderNotificationUseCase
import com.michasoft.thelasttime.notification.ShowNotificationUseCase
import com.michasoft.thelasttime.repo.EventRepository
import com.michasoft.thelasttime.repo.ReminderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

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
    lateinit var localNotificationSource: RoomNotificationSource

    @Inject
    lateinit var showReminderUseCase: ShowReminderUseCase

    override fun onReceive(context: Context?, intent: Intent?) {
        val reminderId = intent?.getStringExtra(REMINDER_ID) ?: return
        (context?.applicationContext as LastTimeApplication?)?.userSessionComponent?.inject(this)
        CoroutineScope(Dispatchers.Main).launch {
            showReminderUseCase.invoke(reminderId)
        }
    }

    companion object {
        const val REMINDER_ID = "reminderId"
    }
}