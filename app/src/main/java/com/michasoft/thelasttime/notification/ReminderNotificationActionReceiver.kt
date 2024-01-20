package com.michasoft.thelasttime.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.michasoft.thelasttime.LastTimeApplication
import com.michasoft.thelasttime.repo.EventRepository
import com.michasoft.thelasttime.useCase.InsertEventInstanceUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReminderNotificationActionReceiver : BroadcastReceiver() {
    @Inject
    lateinit var insertEventInstanceUseCase: InsertEventInstanceUseCase

    @Inject
    lateinit var eventRepository: EventRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        val eventId = intent?.getStringExtra(EVENT_ID) ?: return
        (context?.applicationContext as LastTimeApplication?)?.userSessionComponent?.inject(this)
        CoroutineScope(Dispatchers.Main).launch {
            val createEventInstance = eventRepository.createEventInstance(eventId)
            insertEventInstanceUseCase.execute(createEventInstance)
        }
        context ?: return
        val notificationId = intent.getIntExtra(NOTIFICATION_ID, -1)
        if (notificationId == -1) {
            return
        }
        hideNotification(context, notificationId)
    }

    private fun hideNotification(context: Context, notificationId: Int) {
        with(NotificationManagerCompat.from(context)) {
            cancel(notificationId)
        }
    }

    companion object {
        const val EVENT_ID = "eventId"
        const val NOTIFICATION_ID = "notificationId"
    }
}