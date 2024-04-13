package com.michasoft.thelasttime.reminder

import android.app.NotificationManager
import android.content.Context
import com.michasoft.thelasttime.dataSource.RoomNotificationSource
import javax.inject.Inject

class ReshowRemindersUseCase @Inject constructor(
    private val context: Context,
    private val localNotificationSource: RoomNotificationSource,
    private val showReminderUseCase: ShowReminderUseCase
) {
    suspend fun invoke() {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val activeNotificationIds = notificationManager.activeNotifications.map { it.id }
        localNotificationSource.getAllNotificationInstances().forEach { notificationInstance ->
            if (notificationInstance.notificationId !in activeNotificationIds) {
                localNotificationSource.deleteNotificationInstance(notificationInstance.notificationId)
                val reminderId = notificationInstance.reminderId
                showReminderUseCase.invoke(reminderId)
            }
        }
    }
}