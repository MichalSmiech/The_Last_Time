package com.michasoft.thelasttime.model.repo

import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.Event

/**
 * Created by mśmiech on 02.05.2021.
 */
interface IEventRepository {
    suspend fun getEventTypes(): ArrayList<Event>
    suspend fun getEventType(eventTypeId: Long): Event
    suspend fun getEvents(eventTypeId: Long): List<EventInstance>
    fun save(Event: Event)
    suspend fun getEvent(eventId: Long): EventInstance

}