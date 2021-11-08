package com.michasoft.thelasttime.model.repo

import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.dataSource.ILocalEventSource
import com.michasoft.thelasttime.model.dataSource.IRemoteEventSource
import com.michasoft.thelasttime.util.BackupConfig
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import java.util.*

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

    override suspend fun clearRemoteDatabase() {
        remoteSource.clear()
    }

    override suspend fun copyRemoteToLocal() {
        remoteSource.getAllEvents()
            .buffer(100)
            .collect { event ->
                event.eventInstanceSchema = remoteSource.getEventInstanceSchema(event.id)
                localSource.insertEvent(event)
                remoteSource.getAllEventInstances(event.id, event.eventInstanceSchema!!)
                    .buffer(100)
                    .collect { instance ->
                        localSource.insertEventInstance(instance)
                    }
            }
    }

    override suspend fun copyLocalToRemote() {
        localSource.getAllEvents()
            .buffer(10)
            .collect { event ->
                event.eventInstanceSchema = localSource.getEventInstanceSchema(event.id)
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