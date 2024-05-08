package com.michasoft.thelasttime.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.michasoft.thelasttime.storage.entity.RepeatedReminderEntity
import com.michasoft.thelasttime.storage.entity.SingleReminderEntity
import org.joda.time.DateTime

@Dao
interface ReminderDao {
    @Insert
    suspend fun insertSingleReminder(reminder: SingleReminderEntity)

    @Query("UPDATE ${SingleReminderEntity.TABLE_NAME} SET triggerDateTime=:triggerDateTime WHERE id=:id")
    suspend fun updateSingleReminderTriggerDateTime(id: String, triggerDateTime: DateTime?)

    @Query("SELECT * FROM ${SingleReminderEntity.TABLE_NAME} WHERE id=:id")
    suspend fun getSingleReminder(id: String): SingleReminderEntity?

    @Query("SELECT * FROM ${SingleReminderEntity.TABLE_NAME}")
    suspend fun getSingleReminders(): List<SingleReminderEntity>

    @Query("SELECT * FROM ${SingleReminderEntity.TABLE_NAME} WHERE eventId=:eventId")
    suspend fun getEventSingleReminders(eventId: String): List<SingleReminderEntity>

    @Query("DELETE FROM ${SingleReminderEntity.TABLE_NAME} WHERE id=:id")
    suspend fun deleteSingleReminder(id: String)

    @Query("DELETE FROM ${SingleReminderEntity.TABLE_NAME}")
    suspend fun deleteAllSingleReminders()

    @Query("SELECT COUNT (*) FROM ${SingleReminderEntity.TABLE_NAME} LIMIT 1")
    suspend fun hasSingleReminders(): Int

    @Insert
    suspend fun insertRepeatedReminder(reminder: RepeatedReminderEntity)

    @Query("UPDATE ${RepeatedReminderEntity.TABLE_NAME} SET triggerDateTime=:triggerDateTime WHERE id=:id")
    suspend fun updateRepeatedReminderTriggerDateTime(id: String, triggerDateTime: DateTime?)

    @Query("SELECT * FROM ${RepeatedReminderEntity.TABLE_NAME} WHERE id=:id")
    suspend fun getRepeatedReminder(id: String): RepeatedReminderEntity?

    @Query("SELECT * FROM ${RepeatedReminderEntity.TABLE_NAME}")
    suspend fun getRepeatedReminders(): List<RepeatedReminderEntity>

    @Query("SELECT * FROM ${RepeatedReminderEntity.TABLE_NAME} WHERE eventId=:eventId")
    suspend fun getEventRepeatedReminders(eventId: String): List<RepeatedReminderEntity>

    @Query("DELETE FROM ${RepeatedReminderEntity.TABLE_NAME} WHERE id=:id")
    suspend fun deleteRepeatedReminder(id: String)

    @Query("DELETE FROM ${RepeatedReminderEntity.TABLE_NAME}")
    suspend fun deleteAllRepeatedReminders()

    @Query("SELECT COUNT (*) FROM ${RepeatedReminderEntity.TABLE_NAME} LIMIT 1")
    suspend fun hasRepeatedReminders(): Int
}