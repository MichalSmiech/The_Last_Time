package com.michasoft.thelasttime.permission

import android.Manifest
import android.annotation.SuppressLint
import javax.inject.Inject

class EnsurePostNotificationPermissionUseCase @Inject constructor(
    private val requestPermissionActionBus: RequestPermissionActionBus,
    private val checkPostNotificationPermissionUseCase: CheckPostNotificationPermissionUseCase,
) {
    @SuppressLint("InlinedApi")
    suspend fun execute(): Boolean {
        if (checkPostNotificationPermissionUseCase.execute()) {
            return true
        }
        val permission = Manifest.permission.POST_NOTIFICATIONS
        return requestPermissionActionBus.request(permission)
    }
}