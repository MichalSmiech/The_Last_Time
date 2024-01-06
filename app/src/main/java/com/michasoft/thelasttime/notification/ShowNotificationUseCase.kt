package com.michasoft.thelasttime.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import androidx.activity.result.ActivityResultRegistry
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import javax.inject.Inject

class ShowNotificationUseCase @Inject constructor(
    private val context: Context,
    private val checkPostNotificationPermissionUseCase: CheckPostNotificationPermissionUseCase,
    private val requestPostNotificationPermissionUseCase: RequestPostNotificationPermissionUseCase
) {
    @SuppressLint("MissingPermission")
    suspend operator fun invoke(
        notification: Notification,
        notificationId: Int,
        lifecycle: Lifecycle,
        registry: ActivityResultRegistry
    ) {
        if (checkPostNotificationPermissionUseCase.invoke().not()) {
            val granted = requestPostNotificationPermissionUseCase.execute(lifecycle, registry)
            if (granted.not()) {
                return
            }
        }
        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, notification)
        }
    }
}