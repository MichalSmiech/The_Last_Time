package com.michasoft.thelasttime.notification

import com.michasoft.thelasttime.dataSource.RoomNotificationSource
import javax.inject.Inject

class CancelReminderNotificationsUseCase @Inject constructor(
    private val cancelNotificationUseCase: CancelNotificationUseCase,
    private val localNotificationSource: RoomNotificationSource
) {
    suspend fun invoke(reminderId: String) {
        localNotificationSource.getReminderNotificationInstances(reminderId).forEach {
            cancelNotificationUseCase.invoke(it.notificationId)
        }
    }
}