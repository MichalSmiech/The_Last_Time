package com.michasoft.thelasttime.dataSource

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
    fun getAllEvents(): Flow<Event>
    suspend fun updateEvent(event: Event)
    suspend fun deleteEvent(eventId: String)
    suspend fun deleteAllEvents()

    suspend fun getEventInstanceSchema(eventId: String): EventInstanceSchema

    suspend fun insertEventInstance(instance: EventInstance)
    suspend fun insertEventInstances(instances: List<EventInstance>)
    suspend fun getEventInstance(eventId: String, instanceSchema: EventInstanceSchema, instanceId: String): EventInstance?
    fun getAllEventInstances(eventId: String, instanceSchema: EventInstanceSchema): Flow<EventInstance>
    suspend fun updateEventInstance(instance: EventInstance)
    suspend fun deleteEventInstance(instance: EventInstance)
    suspend fun deleteEventInstance(eventId: String, instanceId: String)







}