package com.michasoft.thelasttime.model

import android.content.Context
import com.michasoft.thelasttime.model.syncJob.SyncJob
import com.michasoft.thelasttime.userSessionComponent
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber

/**
 * Created by mśmiech on 29.01.2023.
 */
class SyncJobQueueCoordinator(
    private val context: Context,
    private val syncJobQueue: SyncJobQueue
) {
    var isActive = false
    private val activeMutex = Mutex()

    suspend fun triggerSync() {
        activeMutex.withLock {
            if (isActive) {
                return
            } else {
                isActive = true
            }
        }
        kotlin.runCatching {
            sync()
        }.onFailure {
            Timber.e("sync failed.", it)
        }
        activeMutex.withLock {
            this@SyncJobQueueCoordinator.isActive = false
        }
    }

    private suspend fun sync() {
        var syncJob = syncJobQueue.firstOrNull()
        while (syncJob != null) {
            syncJobQueue.updateSyncJobState(syncJob, SyncJob.State.Started)
            syncJob.inject(context.userSessionComponent())
            kotlin.runCatching {
                syncJob!!.run()
            }.onSuccess {
                when (it) {
                    SyncJob.State.Completed -> {
                        syncJobQueue.remove(syncJob!!)
                        syncJob = syncJobQueue.firstOrNull()
                    }

                    SyncJob.State.Error -> {
                        syncJobQueue.updateSyncJobState(syncJob!!, SyncJob.State.Error)
                        return
                    }

                    else -> {
                        Timber.e("Unhandled sync job state ($it)")
                        syncJobQueue.updateSyncJobState(syncJob!!, SyncJob.State.Error)
                        return
                    }
                }
            }.onFailure {
                Timber.e("run syncJob failed", it)
                syncJobQueue.updateSyncJobState(syncJob!!, SyncJob.State.Error)
                return
            }
        }
    }
}