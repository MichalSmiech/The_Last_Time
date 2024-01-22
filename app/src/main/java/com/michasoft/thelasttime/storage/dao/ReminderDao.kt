package com.michasoft.thelasttime.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.michasoft.thelasttime.storage.entity.RepeatedReminderEntity
import com.michasoft.thelasttime.storage.entity.SingleReminderEntity

@Dao
interface ReminderDao {
    @Insert
    suspend fun insertSingleReminder(reminder: SingleReminderEntity)

    @Query("SELECT * FROM ${SingleReminderEntity.TABLE_NAME} WHERE id=:id")
    suspend fun getSingleReminder(id: String): SingleReminderEntity?

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

    @Query("UPDATE ${RepeatedReminderEntity.TABLE_NAME} SET label=:label WHERE id=:id")
    suspend fun updateRepeatedReminderLabel(id: String, label: String)

    @Query("SELECT * FROM ${RepeatedReminderEntity.TABLE_NAME} WHERE id=:id")
    suspend fun getRepeatedReminder(id: String): RepeatedReminderEntity?

    @Query("SELECT * FROM ${RepeatedReminderEntity.TABLE_NAME} WHERE eventId=:eventId")
    suspend fun getEventRepeatedReminders(eventId: String): List<RepeatedReminderEntity>

    @Query("DELETE FROM ${RepeatedReminderEntity.TABLE_NAME} WHERE id=:id")
    suspend fun deleteRepeatedReminder(id: String)

    @Query("DELETE FROM ${RepeatedReminderEntity.TABLE_NAME}")
    suspend fun deleteAllRepeatedReminders()

    @Query("SELECT COUNT (*) FROM ${RepeatedReminderEntity.TABLE_NAME} LIMIT 1")
    suspend fun hasRepeatedReminders(): Int
}