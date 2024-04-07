package com.michasoft.thelasttime.notification

import com.michasoft.thelasttime.dataSource.RoomNotificationSource
import javax.inject.Inject

class CancelAllNotificationsUseCase @Inject constructor(
    private val cancelNotificationUseCase: CancelNotificationUseCase,
    private val localNotificationSource: RoomNotificationSource
) {
    suspend fun invoke() {
        localNotificationSource.getAllNotificationInstances().forEach {
            cancelNotificationUseCase.invoke(it.notificationId)
        }
    }
}