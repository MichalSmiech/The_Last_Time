package com.michasoft.thelasttime.reminder

import android.app.NotificationManager
import android.content.Context
import com.michasoft.thelasttime.dataSource.RoomNotificationSource
import com.michasoft.thelasttime.repo.ReminderRepository
import javax.inject.Inject

class ReshowRemindersUseCase @Inject constructor(
    private val context: Context,
    private val localNotificationSource: RoomNotificationSource,
    private val showReminderUseCase: ShowReminderUseCase,
    private val reminderRepository: ReminderRepository
) {
    suspend fun invoke() {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val reminderIds = mutableSetOf<String>()

        val activeNotificationIds = notificationManager.activeNotifications.map { it.id }
        localNotificationSource.getAllNotificationInstances().forEach { notificationInstance ->
            reminderIds.add(notificationInstance.reminderId)
            if (notificationInstance.notificationId !in activeNotificationIds) {
                localNotificationSource.deleteNotificationInstance(notificationInstance.notificationId)
                val reminderId = notificationInstance.reminderId
                val reminder = reminderRepository.getReminder(reminderId)
                if (reminder?.reshowEnabled == true) {
                    showReminderUseCase.invoke(reminderId)
                }
            }
        }

        reminderRepository.getReminders()
            .filter { it.id !in reminderIds && it.isTriggerDateTimePassed && it.reshowEnabled }
            .forEach { reminder ->
                showReminderUseCase.invoke(reminder.id)
            }
    }
}