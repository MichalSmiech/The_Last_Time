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

    @Query("UPDATE ${SingleReminderEntity.TABLE_NAME} SET dateTime=:dateTime, label=:label WHERE id=:id")
    suspend fun updateSingleReminder(id: String, dateTime: DateTime, label: String)

    @Query("SELECT * FROM ${SingleReminderEntity.TABLE_NAME} WHERE id=:id")
    suspend fun getSingleReminder(id: String): SingleReminderEntity?

    @Query("SELECT * FROM ${SingleReminderEntity.TABLE_NAME} WHERE eventId=:eventId")
    suspend fun getEventSingleReminders(eventId: String): List<SingleReminderEntity>

    @Query("DELETE FROM ${SingleReminderEntity.TABLE_NAME} WHERE id=:id")
    suspend fun deleteSingleReminder(id: String)

    @Insert
    suspend fun insertRepeatedReminder(reminder: RepeatedReminderEntity)

    @Query("UPDATE ${RepeatedReminderEntity.TABLE_NAME} SET periodText=:periodText WHERE id=:id")
    suspend fun updateRepeatedReminder(id: String, periodText: String)

    @Query("SELECT * FROM ${RepeatedReminderEntity.TABLE_NAME} WHERE id=:id")
    suspend fun getRepeatedReminder(id: String): RepeatedReminderEntity?

    @Query("SELECT * FROM ${RepeatedReminderEntity.TABLE_NAME} WHERE eventId=:eventId")
    suspend fun getEventRepeatedReminders(eventId: String): List<RepeatedReminderEntity>

    @Query("DELETE FROM ${RepeatedReminderEntity.TABLE_NAME} WHERE id=:id")
    suspend fun deleteRepeatedReminder(id: String)
}