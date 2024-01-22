package com.michasoft.thelasttime.permission

import android.Manifest
import android.os.Build
import javax.inject.Inject

class CheckPostNotificationPermissionUseCase @Inject constructor(
    private val checkPermissionUseCase: CheckPermissionUseCase
) {
    fun execute(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return true
        }
        val permission = Manifest.permission.POST_NOTIFICATIONS
        return checkPermissionUseCase.execute(permission)
    }
}