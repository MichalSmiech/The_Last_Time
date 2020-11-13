package com.michasoft.thelasttime.model.repo.storage.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.michasoft.thelasttime.model.repo.storage.entity.EventEntity

/**
 * Created by mśmiech on 11.11.2020.
 */

@Dao
interface EventsDao {
    @Insert
    suspend fun insertEvent(event: EventEntity): Long
    @Update
    suspend fun updateEvent(event: EventEntity)
    @Delete
    suspend fun deleteEvent(event: EventEntity)
    @Query("SELECT * FROM ${EventEntity.TABLE_NAME}")
    fun getEvents() : LiveData<List<EventEntity>>
}