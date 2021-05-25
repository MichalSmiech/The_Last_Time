package com.michasoft.thelasttime.model.repo

import com.michasoft.thelasttime.model.EventType
import org.joda.time.DateTime

/**
 * Created by mśmiech on 11.11.2020.
 */
class EventsRepository: IEventsRepository {
    private val eventTypes = ArrayList<EventType>()

    init {
        eventTypes.add(EventType(1L, "Plants", DateTime.now()))
        eventTypes.add(EventType(2L, "Vacuum", DateTime.now().minusDays(3)))
    }

    override fun getEventTypes(): ArrayList<EventType> {
        return eventTypes
    }
}