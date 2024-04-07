package com.michasoft.thelasttime.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.michasoft.thelasttime.storage.entity.NotificationInstanceEntity

@Dao
interface NotificationDao {
    @Insert
    suspend fun insertNotificationInstance(notificationInstance: NotificationInstanceEntity)

    @Query("DELETE FROM ${NotificationInstanceEntity.TABLE_NAME} WHERE notificationId=:notificationId")
    suspend fun deleteNotificationInstance(notificationId: Int)

    @Query("SELECT * FROM ${NotificationInstanceEntity.TABLE_NAME} WHERE reminderId = :reminderId")
    suspend fun getReminderNotificationInstances(reminderId: String): List<NotificationInstanceEntity>

    @Query("SELECT * FROM ${NotificationInstanceEntity.TABLE_NAME}")
    fun getAllNotificationInstances(): List<NotificationInstanceEntity>
}