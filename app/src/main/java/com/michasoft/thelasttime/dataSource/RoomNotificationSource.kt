package com.michasoft.thelasttime.dataSource

import com.michasoft.thelasttime.di.UserSessionScope
import com.michasoft.thelasttime.model.NotificationInstance
import com.michasoft.thelasttime.storage.dao.NotificationDao
import com.michasoft.thelasttime.storage.entity.toEntity
import javax.inject.Inject

@UserSessionScope
class RoomNotificationSource @Inject constructor(
    private val notificationDao: NotificationDao
) {
    suspend fun insertNotificationInstance(notificationInstance: NotificationInstance) {
        notificationDao.insertNotificationInstance(notificationInstance.toEntity())
    }

    suspend fun deleteNotificationInstance(notificationId: Int) {
        notificationDao.deleteNotificationInstance(notificationId)
    }

    suspend fun getReminderNotificationInstances(reminderId: String): List<NotificationInstance> {
        return notificationDao.getReminderNotificationInstances(reminderId).map { it.toModel() }
    }

    suspend fun getAllNotificationInstances(): List<NotificationInstance> {
        return notificationDao.getAllNotificationInstances().map { it.toModel() }
    }
}