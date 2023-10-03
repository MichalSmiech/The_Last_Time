package com.michasoft.thelasttime.dataSource

import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.EventInstanceSchema
import com.michasoft.thelasttime.model.Label
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

    suspend fun getEventInstanceSchema(eventId: String): EventInstanceSchema

    suspend fun insertEventInstance(instance: EventInstance)
    suspend fun getEventInstance(eventId: String, instanceId: String): EventInstance?
    suspend fun getAllEventInstances(
        eventId: String,
        eventInstanceSchema: EventInstanceSchema
    ): Flow<EventInstance>

    suspend fun getAllEventInstancesAtOnce(eventId: String): ArrayList<EventInstance>
    suspend fun getEventInstance(
        instanceSchema: EventInstanceSchema,
        instanceId: String
    ): EventInstance?

    suspend fun deleteEventInstance(instance: EventInstance)
    suspend fun deleteEventInstance(eventId: String, instanceId: String)
    suspend fun getLastInstanceTimestamp(eventId: String): DateTime?
    suspend fun updateEventInstance(instance: EventInstance)
    suspend fun updateEvent(event: Event)

    suspend fun insertLabel(label: Label)
    suspend fun updateLabelName(labelId: String, name: String)
    suspend fun deleteLabel(labelId: String)
    suspend fun insertEventLabel(eventId: String, labelId: String)
    suspend fun deleteEventLabel(eventId: String, labelId: String)
    suspend fun getEventLabels(eventId: String): List<Label>
}