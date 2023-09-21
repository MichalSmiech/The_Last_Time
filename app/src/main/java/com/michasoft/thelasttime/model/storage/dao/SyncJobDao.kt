package com.michasoft.thelasttime.model.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.michasoft.thelasttime.model.storage.entity.SyncJobEntity
import com.michasoft.thelasttime.model.syncJob.SyncJob

/**
 * Created by mśmiech on 21.09.2023.
 */
@Dao
interface SyncJobDao {
    @Insert
    suspend fun insertSyncJob(syncJobEntity: SyncJobEntity)

    @Query("SELECT * FROM ${SyncJobEntity.TABLE_NAME}")
    suspend fun getSyncJobs(): List<SyncJobEntity>

    @Query("DELETE FROM ${SyncJobEntity.TABLE_NAME} WHERE id=:id")
    suspend fun deleteSyncJob(id: String)

    @Query("UPDATE ${SyncJobEntity.TABLE_NAME} SET state = :state WHERE id = :id")
    suspend fun updateSyncJobState(id: String, state: SyncJob.State)
}