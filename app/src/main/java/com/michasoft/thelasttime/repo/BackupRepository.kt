package com.michasoft.thelasttime.repo

import com.michasoft.thelasttime.dataSource.ILocalEventSource
import com.michasoft.thelasttime.dataSource.IRemoteEventSource
import com.michasoft.thelasttime.model.EventInstance
import kotlinx.coroutines.flow.buffer

/**
 * Created by mśmiech on 08.11.2021.
 */
class BackupRepository(
    private val localSource: ILocalEventSource,
    private val remoteSource: IRemoteEventSource
) : IBackupRepository {

    override suspend fun clearLocalDatabase() {
        localSource.clear()
    }

    override suspend fun clearBackup() {
        remoteSource.clear()
    }

    override suspend fun restoreBackup() {
        remoteSource.getAllEvents()
            .buffer(100)
            .collect { event ->
                localSource.insertEvent(event)
                remoteSource.getAllEventInstances(event.id, event.eventInstanceSchema!!)
                    .buffer(100)
                    .collect { instance ->
                        localSource.insertEventInstance(instance)
                    }
            }
    }

    override suspend fun makeBackup() {
        localSource.getAllEvents()
            .buffer(10)
            .collect { event ->
                remoteSource.insertEvent(event)
                val buffer = ArrayList<EventInstance>(100)
                localSource.getAllEventInstances(event.id, event.eventInstanceSchema!!)
                    .buffer(100)
                    .collect {
                        buffer.add(it)
                        if (buffer.size == 100) {
                            remoteSource.insertEventInstances(buffer)
                            buffer.clear()
                        }
                    }
            }
    }

}