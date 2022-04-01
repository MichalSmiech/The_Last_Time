package com.michasoft.thelasttime.model.repo

import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstanceSchema

/**
 * Created by mśmiech on 02.05.2021.
 */
interface IEventRepository {
    suspend fun insertEvent(event: Event)
    suspend fun getEvent(eventId: String): Event?
    suspend fun getEvents(): ArrayList<Event>
    suspend fun getEventsWithLastInstanceTimestamp(): ArrayList<Event>
    suspend fun update(event: Event)

    suspend fun createEventInstance(eventId: String): EventInstance
    suspend fun insert(instance: EventInstance)
    suspend fun getEventInstance(eventId: String, instanceId: String): EventInstance?
    suspend fun getEventInstances(eventId: String): List<EventInstance>
    suspend fun update(instance: EventInstance)

    suspend fun getEventInstanceSchema(eventId: String): EventInstanceSchema

}