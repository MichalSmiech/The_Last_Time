package com.michasoft.thelasttime.model.repo

import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.Event
import org.joda.time.DateTime
import java.lang.IllegalStateException
import java.util.Collections.max

/**
 * Created by mśmiech on 11.11.2020.
 */
class MockEventRepository: IEventRepository {
    private val eventTypeMap = mutableMapOf<Long, Event>()
    override suspend fun insertEvent(event: Event) {
        TODO("Not yet implemented")
    }

    override suspend fun insertEventInstance(instance: EventInstance) {
        TODO("Not yet implemented")
    }

    private val eventsMap = mutableMapOf<Long, List<EventInstance>>()

    init {
//        eventTypeMap[1L] = Event(1L, "Plants", DateTime.now())
//        eventTypeMap[2L] = Event(2L, "Vacuum", DateTime.now().minusDays(3))
    }

    override suspend fun getEvents(): ArrayList<Event> {
        return ArrayList(eventTypeMap.values.toList())
    }

    override suspend fun getEventInstance(eventId: Long, instanceId: Long): EventInstance {
        return EventInstance(1, instanceId, DateTime.now(), ArrayList())
    }

    override suspend fun getEvent(eventId: Long): Event {
        return eventTypeMap[eventId]?.copy() ?: createNewEventType()
    }

    override suspend fun getEventInstances(eventId: Long): List<EventInstance> {
        if(eventsMap.containsKey(eventId)) {
            return eventsMap[eventId]!!
        }
        val result = arrayListOf<EventInstance>()
//        for (i in 0..Random.nextInt(5, 20)) {
//            result.add(EventInstance(Random.nextLong(), DateTime.now().minusDays(Random.nextInt(2, 100)), eventTypeId))
//        }
        result.sortBy { event -> event.timestamp.millis }
        eventsMap[eventId] = result
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