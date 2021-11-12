package com.michasoft.thelasttime.model.repo

import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.EventInstanceSchema
import com.michasoft.thelasttime.model.dataSource.ILocalEventSource
import com.michasoft.thelasttime.model.dataSource.IRemoteEventSource
import com.michasoft.thelasttime.util.BackupConfig
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first

/**
 * Created by mśmiech on 01.11.2021.
 */
class EventRepository(
    private val localSource: ILocalEventSource,
    private val remoteSource: IRemoteEventSource,
    private val backupConfig: BackupConfig
): IEventRepository {

    override suspend fun insertEvent(event: Event) {
        localSource.insertEvent(event)
        if(backupConfig.isAutoBackup()) {
            remoteSource.insertEvent(event)
        }
    }

    override suspend fun getEvent(eventId: String): Event? {
        return localSource.getEvent(eventId)
    }

    override suspend fun getEvents(): ArrayList<Event> {
        return localSource.getAllEventsAtOnce()
    }

    override suspend fun getEventInstanceSchema(eventId: String): EventInstanceSchema {
        return localSource.getEventInstanceSchema(eventId)
    }

    override suspend fun getEventsWithLastInstanceTimestamp(): ArrayList<Event> {
        val events = getEvents()
        events.forEach { event ->
            event.lastInstanceTimestamp = localSource.getLastInstanceTimestamp(event.id)
        }
        return events
    }

    override fun save(Event: Event) {
        TODO("Not yet implemented")
    }

    override suspend fun insertEventInstance(instance: EventInstance) {
        localSource.insertEventInstance(instance)
        if (backupConfig.isAutoBackup()) {
            remoteSource.insertEventInstance(instance)
        }
    }

    override suspend fun getEventInstance(eventId: String, instanceId: String): EventInstance? {
        return localSource.getEventInstance(eventId, instanceId)
    }

    override suspend fun getEventInstances(eventId: String): List<EventInstance> {
        return localSource.getAllEventInstancesAtOnce(eventId)
    }
}