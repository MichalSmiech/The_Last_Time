package com.michasoft.thelasttime.model.repo

import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.Event

/**
 * Created by mśmiech on 02.05.2021.
 */
interface IEventRepository {
    suspend fun insertEvent(event: Event)
    suspend fun getEvent(eventId: Long): Event?
    suspend fun getEvents(): ArrayList<Event>
    fun save(Event: Event)

    suspend fun insertEventInstance(instance: EventInstance)
    suspend fun getEventInstance(eventId: Long, instanceId: Long): EventInstance?
    suspend fun getEventInstances(eventId: Long): List<EventInstance>

}