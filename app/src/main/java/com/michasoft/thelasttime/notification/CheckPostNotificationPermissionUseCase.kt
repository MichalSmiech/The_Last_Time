package com.michasoft.thelasttime.notification

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import javax.inject.Inject

/**
 * Created by mśmiech on 11.06.2023.
 */
class CheckPostNotificationPermissionUseCase @Inject constructor(
    private val context: Context
) {
    operator fun invoke(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            return true
        }
    }
}