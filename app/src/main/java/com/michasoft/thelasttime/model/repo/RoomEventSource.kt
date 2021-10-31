package com.michasoft.thelasttime.model.repo

import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventType

/**
 * Created by mśmiech on 05.09.2021.
 */
class RoomEventSource: IEventSource {
    override suspend fun getEventType(eventTypeId: Long): EventType {
        TODO("Not yet implemented")
    }

    override suspend fun getEvent(eventId: Long): Event {
        TODO("Not yet implemented")
    }

    override suspend fun saveEvent(event: Event) {
        TODO("Not yet implemented")
    }

    override suspend fun saveEventType(eventType: EventType) {
        TODO("Not yet implemented")
    }
}