package com.michasoft.thelasttime.dataSource

import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.EventInstanceSchema
import kotlinx.coroutines.flow.Flow
import org.joda.time.DateTime

/**
 * Created by mśmiech on 01.11.2021.
 */
interface ILocalEventSource {
    suspend fun clear()

    suspend fun insertEvent(event: Event)
    suspend fun getEvent(eventId: String): Event?
    suspend fun getAllEvents(): Flow<Event>
    suspend fun getAllEventsAtOnce(): ArrayList<Event>
    suspend fun deleteEvent(eventId: String)
    suspend fun deleteAllEvents()

    suspend fun getEventInstanceSchema(eventId: String): EventInstanceSchema

    suspend fun insertEventInstance(instance: EventInstance)
    suspend fun getEventInstance(eventId: String, instanceId: String): EventInstance?
    suspend fun getAllEventInstances(eventId: String, eventInstanceSchema: EventInstanceSchema): Flow<EventInstance>
    suspend fun getAllEventInstancesAtOnce(eventId: String): ArrayList<EventInstance>
    suspend fun getEventInstance(instanceSchema: EventInstanceSchema, instanceId: String): EventInstance?
    suspend fun deleteEventInstance(instance: EventInstance)
    suspend fun deleteEventInstance(eventId: String, instanceId: String)
    suspend fun getLastInstanceTimestamp(eventId: String): DateTime?
    suspend fun updateEventInstance(instance: EventInstance)
    suspend fun updateEvent(event: Event)
}