package com.michasoft.thelasttime.model.repo

import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventType
import org.joda.time.DateTime
import java.lang.IllegalStateException
import java.util.Collections.max
import kotlin.random.Random

/**
 * Created by mśmiech on 11.11.2020.
 */
class MockEventRepository: IEventRepository {
    private val eventTypeMap = mutableMapOf<Long, EventType>()
    private val eventsMap = mutableMapOf<Long, List<Event>>()

    init {
        eventTypeMap[1L] = EventType(1L, "Plants", DateTime.now())
        eventTypeMap[2L] = EventType(2L, "Vacuum", DateTime.now().minusDays(3))
    }

    override suspend fun getEventTypes(): ArrayList<EventType> {
        return ArrayList(eventTypeMap.values.toList())
    }

    override suspend fun getEvent(eventId: Long): Event {
        return Event(eventId, DateTime.now(), 1L)
    }

    override suspend fun getEventType(eventTypeId: Long): EventType {
        return eventTypeMap[eventTypeId]?.copy() ?: createNewEventType()
    }

    override suspend fun getEvents(eventTypeId: Long): List<Event> {
        if(eventsMap.containsKey(eventTypeId)) {
            return eventsMap[eventTypeId]!!
        }
        val result = arrayListOf<Event>()
        for (i in 0..Random.nextInt(5, 20)) {
            result.add(Event(Random.nextLong(), DateTime.now().minusDays(Random.nextInt(2, 100)), eventTypeId))
        }
        result.sortBy { event -> event.timestamp.millis }
        eventsMap[eventTypeId] = result
        return result
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