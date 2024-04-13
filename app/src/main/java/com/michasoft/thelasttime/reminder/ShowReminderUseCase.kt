package com.michasoft.thelasttime.reminder

import com.michasoft.thelasttime.dataSource.RoomNotificationSource
import com.michasoft.thelasttime.model.NotificationInstance
import com.michasoft.thelasttime.notification.CreateNotificationChannelUseCase
import com.michasoft.thelasttime.notification.CreateReminderNotificationUseCase
import com.michasoft.thelasttime.notification.NotificationChannels
import com.michasoft.thelasttime.notification.ShowNotificationUseCase
import com.michasoft.thelasttime.repo.ReminderRepository
import javax.inject.Inject
import kotlin.math.absoluteValue
import kotlin.random.Random

class ShowReminderUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val createNotificationChannelUseCase: CreateNotificationChannelUseCase,
    private val createReminderNotificationUseCase: CreateReminderNotificationUseCase,
    private val localNotificationSource: RoomNotificationSource,
    private val showNotificationUseCase: ShowNotificationUseCase
) {
    suspend fun invoke(reminderId: String) {
        val reminder = reminderRepository.getReminder(reminderId) ?: return

        val channelData = NotificationChannels.reminderChannelData
        createNotificationChannelUseCase.invoke(channelData)

        val notificationId = Random.nextInt().absoluteValue + 1
        val notification = createReminderNotificationUseCase.invoke(reminder, notificationId) ?: return

        localNotificationSource.insertNotificationInstance(NotificationInstance(notificationId, reminderId))

        showNotificationUseCase.invoke(notification, notificationId)
    }
}