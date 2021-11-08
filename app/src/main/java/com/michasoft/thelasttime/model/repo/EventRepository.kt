package com.michasoft.thelasttime.model.repo

import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.dataSource.ILocalEventSource
import com.michasoft.thelasttime.model.dataSource.IRemoteEventSource

/**
 * Created by mśmiech on 01.11.2021.
 */
class EventRepository(private val localSource: ILocalEventSource, private val remoteSource: IRemoteEventSource): IEventRepository {
    override suspend fun insertEvent(event: Event) {
        localSource.insertEvent(event)
        remoteSource.insertEvent(event)
    }

    override suspend fun getEvent(eventId: Long): Event? {
        return localSource.getEvent(eventId)
    }

    override suspend fun getEvents(): ArrayList<Event> {
        TODO("Not yet implemented")
    }

    override fun save(Event: Event) {
        TODO("Not yet implemented")
    }

    override suspend fun insertEventInstance(instance: EventInstance) {
        localSource.insertEventInstance(instance)
        remoteSource.insertEventInstance(instance)
    }

    override suspend fun getEventInstance(eventId: Long, instanceId: Long): EventInstance? {
        return localSource.getEventInstance(eventId, instanceId)
    }

    override suspend fun getEventInstances(eventId: Long): List<EventInstance> {
        TODO("Not yet implemented")
    }
}