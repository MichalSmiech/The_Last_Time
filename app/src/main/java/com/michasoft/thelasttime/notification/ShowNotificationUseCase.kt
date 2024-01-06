package com.michasoft.thelasttime.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import javax.inject.Inject

class ShowNotificationUseCase @Inject constructor(
    private val context: Context,
    private val checkPostNotificationPermissionUseCase: CheckPostNotificationPermissionUseCase,
) {
    @SuppressLint("MissingPermission")
    suspend operator fun invoke(
        notification: Notification,
        notificationId: Int,
    ) {
        if (checkPostNotificationPermissionUseCase.invoke().not()) {
            return
        }
        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, notification)
        }
    }
}