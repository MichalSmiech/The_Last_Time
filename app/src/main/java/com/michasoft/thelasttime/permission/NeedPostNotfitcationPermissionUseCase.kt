package com.michasoft.thelasttime.permission

import com.michasoft.thelasttime.dataSource.RoomReminderSource
import javax.inject.Inject

class NeedRequestPostNotificationPermissionUseCase @Inject constructor(
    private val localReminderSource: RoomReminderSource,
    private val checkPostNotificationPermissionUseCase: CheckPostNotificationPermissionUseCase,
) {
    suspend fun execute(): Boolean {
        if (checkPostNotificationPermissionUseCase.execute()) {
            return false
        }
        return localReminderSource.hasReminders()
    }
}