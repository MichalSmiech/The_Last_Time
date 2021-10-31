package com.michasoft.thelasttime.model.repo

import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventType

/**
 * Created by mśmiech on 05.09.2021.
 */
interface IEventSource {
    suspend fun getEventType(eventTypeId: Long): EventType
    suspend fun getEvent(eventId: Long): Event
    suspend fun saveEvent(event: Event)
    suspend fun saveEventType(eventType: EventType)
}