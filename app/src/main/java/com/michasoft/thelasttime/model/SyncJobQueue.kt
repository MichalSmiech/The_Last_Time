package com.michasoft.thelasttime.model

import com.michasoft.thelasttime.model.syncJob.SyncJob
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Created by mśmiech on 29.01.2023.
 */
class SyncJobQueue {
    private var syncJobs = ArrayList<SyncJob>()
    private val mutex = Mutex()

    suspend fun add(syncJob: SyncJob) {
        mutex.withLock {
            syncJobs.add(syncJob)
        }
    }

    suspend fun firstOrNull(): SyncJob? {
        return mutex.withLock { syncJobs.firstOrNull() }
    }

    suspend fun update(syncJob: SyncJob) {
        mutex.withLock {
            val syncJobIndex = syncJobs.indexOfFirst { it.id == syncJob.id }
            syncJobs[syncJobIndex] = syncJob
        }
    }

    suspend fun updateSyncJobState(syncJob: SyncJob, state: SyncJob.State) {
        mutex.withLock {
            val syncJobIndex = syncJobs.indexOfFirst { it.id == syncJob.id }
            syncJobs[syncJobIndex] = syncJob.copy(state = state)
        }
    }

    suspend fun remove(syncJob: SyncJob) {
        mutex.withLock {
            val syncJobIndex = syncJobs.indexOfFirst { it.id == syncJob.id }
            syncJobs.removeAt(syncJobIndex)
        }
    }
}