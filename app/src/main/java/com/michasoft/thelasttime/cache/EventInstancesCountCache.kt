package com.michasoft.thelasttime.cache

import com.michasoft.thelasttime.dataSource.ILocalEventSource
import org.joda.time.LocalDate
import javax.inject.Inject

class EventInstancesCountCache @Inject constructor(
    private val localEventSource: ILocalEventSource,
) {
    /**
     * eventId to (date to instancesCount)
     */
    private val eventInstancesCountMap = mutableMapOf<String, MutableMap<LocalDate, Int>>()

    suspend fun getEventInstancesCount(eventId: String, date: LocalDate): Int {
        val instancesCount = eventInstancesCountMap[eventId]?.get(date)
        if (instancesCount != null) {
            return instancesCount
        }
        var eventsMap = eventInstancesCountMap[eventId]
        if (eventsMap == null) {
            eventsMap = mutableMapOf<LocalDate, Int>().also { eventInstancesCountMap[eventId] = it }
        }
        return localEventSource.getEventInstancesCount(eventId, date).also {
            eventsMap[date] = it
        }
    }

    fun invalid(eventId: String) {
        eventInstancesCountMap.remove(eventId)
    }
}