package com.michasoft.thelasttime.model.dataSource

import com.michasoft.thelasttime.model.SyncJobFactory
import com.michasoft.thelasttime.model.storage.dao.SyncJobDao
import com.michasoft.thelasttime.model.storage.entity.SyncJobEntity
import com.michasoft.thelasttime.model.syncJob.SyncJob

/**
 * Created by mśmiech on 21.09.2023.
 */
class SyncJobDataSource(private val syncJobDao: SyncJobDao) {
    suspend fun insertSyncJob(syncJob: SyncJob) {
        syncJobDao.insertSyncJob(SyncJobEntity(syncJob))
    }

    suspend fun updateSyncJobState(syncJob: SyncJob, state: SyncJob.State) {
        syncJobDao.updateSyncJobState(syncJob.id, state)
    }

    suspend fun deleteSyncJob(syncJob: SyncJob) {
        syncJobDao.deleteSyncJob(syncJob.id)
    }

    suspend fun getAllSyncJobs(): List<SyncJob> {
        return syncJobDao.getSyncJobs().map {
            SyncJobFactory.create(it.id, it.type, it.action, it.state, it.serializedData)
        }
    }
}