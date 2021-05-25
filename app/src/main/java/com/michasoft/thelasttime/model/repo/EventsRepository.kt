package com.michasoft.thelasttime.model.repo

import com.michasoft.thelasttime.model.EventType
import org.joda.time.DateTime
import java.lang.IllegalStateException
import java.util.Collections.max

/**
 * Created by mśmiech on 11.11.2020.
 */
class EventsRepository: IEventsRepository {
    private val eventTypeMap = mutableMapOf<Long, EventType>()

    init {
        eventTypeMap[1L] = EventType(1L, "Plants", DateTime.now())
        eventTypeMap[2L] = EventType(2L, "Vacuum", DateTime.now().minusDays(3))
    }

    override fun getEventTypes(): ArrayList<EventType> {
        return ArrayList(eventTypeMap.values.toList())
    }

    override fun getEventType(eventTypeId: Long): EventType {
        return eventTypeMap[eventTypeId]?.copy() ?: createNewEventType()
    }

    private fun createNewEventType(): EventType {
        return EventType(0L, "", null)
    }

    override fun save(eventType: EventType) {
        when {
            eventTypeMap.containsKey(eventType.id) -> {
                eventTypeMap[eventType.id] = eventType
            }
            eventType.id == 0L -> {
                eventType.id = getNextFreeEventTypeId()
                eventTypeMap[eventType.id] = eventType
            }
            else -> {
                throw IllegalStateException()
            }
        }
    }

    private fun getNextFreeEventTypeId(): Long {
        return max(eventTypeMap.keys) + 1L
    }
}