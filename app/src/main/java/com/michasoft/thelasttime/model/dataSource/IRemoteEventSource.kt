package com.michasoft.thelasttime.model.dataSource

import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.EventInstanceSchema
import kotlinx.coroutines.flow.Flow

/**
 * Created by mśmiech on 01.11.2021.
 */
interface IRemoteEventSource {
    suspend fun clear()

    suspend fun insertEvent(event: Event)
    suspend fun getEvent(eventId: String): Event?
    suspend fun deleteEvent(eventId: String)

    suspend fun getEventInstanceSchema(eventId: String): EventInstanceSchema

    suspend fun insertEventInstance(instance: EventInstance)
    suspend fun insertEventInstances(instances: List<EventInstance>)
    suspend fun updateEventInstance(instance: EventInstance)
    suspend fun getEventInstance(eventId: String, instanceSchema: EventInstanceSchema, instanceId: String): EventInstance?
    suspend fun deleteEventInstance(instance: EventInstance)

    fun getAllEvents(): Flow<Event>
    fun getAllEventInstances(eventId: String, instanceSchema: EventInstanceSchema): Flow<EventInstance>

    suspend fun deleteAllEvents()
}