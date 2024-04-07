package com.michasoft.thelasttime.notification

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import com.michasoft.thelasttime.dataSource.RoomNotificationSource
import javax.inject.Inject

class CancelNotificationUseCase @Inject constructor(
    private val context: Context,
    private val localNotificationSource: RoomNotificationSource
) {
    suspend fun invoke(notificationId: Int) {
        with(NotificationManagerCompat.from(context)) {
            cancel(notificationId)
        }
        localNotificationSource.deleteNotificationInstance(notificationId)
    }
}