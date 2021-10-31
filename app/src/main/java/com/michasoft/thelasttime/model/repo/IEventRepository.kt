package com.michasoft.thelasttime.model.repo

import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventType

/**
 * Created by mśmiech on 02.05.2021.
 */
interface IEventRepository {
    suspend fun getEventTypes(): ArrayList<EventType>
    suspend fun getEventType(eventTypeId: Long): EventType
    suspend fun getEvents(eventTypeId: Long): List<Event>
    fun save(EventType: EventType)
    suspend fun getEvent(eventId: Long): Event

}