package com.michasoft.thelasttime.model.repo

import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.Event
import org.joda.time.DateTime
import java.lang.IllegalStateException
import java.util.Collections.max
import kotlin.random.Random

/**
 * Created by mśmiech on 11.11.2020.
 */
class MockEventRepository: IEventRepository {
    private val eventTypeMap = mutableMapOf<Long, Event>()
    private val eventsMap = mutableMapOf<Long, List<EventInstance>>()

    init {
//        eventTypeMap[1L] = Event(1L, "Plants", DateTime.now())
//        eventTypeMap[2L] = Event(2L, "Vacuum", DateTime.now().minusDays(3))
    }

    override suspend fun getEventTypes(): ArrayList<Event> {
        return ArrayList(eventTypeMap.values.toList())
    }

    override suspend fun getEvent(eventId: Long): EventInstance {
        return EventInstance(1, eventId, DateTime.now(), ArrayList())
    }

    override suspend fun getEventType(eventTypeId: Long): Event {
        return eventTypeMap[eventTypeId]?.copy() ?: createNewEventType()
    }

    override suspend fun getEvents(eventTypeId: Long): List<EventInstance> {
        if(eventsMap.containsKey(eventTypeId)) {
            return eventsMap[eventTypeId]!!
        }
        val result = arrayListOf<EventInstance>()
//        for (i in 0..Random.nextInt(5, 20)) {
//            result.add(EventInstance(Random.nextLong(), DateTime.now().minusDays(Random.nextInt(2, 100)), eventTypeId))
//        }
        result.sortBy { event -> event.timestamp.millis }
        eventsMap[eventTypeId] = result
        return result
    }

    private fun createNewEventType(): Event {
        return Event(0L, "")
    }

    override fun save(event: Event) {
        when {
            eventTypeMap.containsKey(event.id) -> {
                eventTypeMap[event.id] = event
            }
            event.id == 0L -> {
                event.id = getNextFreeEventTypeId()
                eventTypeMap[event.id] = event
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