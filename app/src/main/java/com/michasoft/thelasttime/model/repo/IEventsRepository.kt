package com.michasoft.thelasttime.model.repo

import com.michasoft.thelasttime.model.EventType

/**
 * Created by mśmiech on 02.05.2021.
 */
interface IEventsRepository {
    fun getEventTypes(): ArrayList<EventType>
    fun getEventType(eventTypeId: Long): EventType
    fun save(EventType: EventType)

}