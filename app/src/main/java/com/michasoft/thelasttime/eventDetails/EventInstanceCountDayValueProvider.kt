package com.michasoft.thelasttime.eventDetails

import com.michasoft.thelasttime.calendarWidget.monthCount.DayValueProvider
import com.michasoft.thelasttime.repo.EventRepository
import org.joda.time.LocalDate
import javax.inject.Inject

class EventInstanceCountDayValueProvider private constructor(
    private val eventId: String,
    private val eventRepository: EventRepository
) : DayValueProvider {
    override suspend fun getNormalizeValue(date: LocalDate): Float {
        val maxValue = getMaxValue()
        if (maxValue == 0) {
            return 0f
        }
        return getValue(date).toFloat() / maxValue
    }

    private suspend fun getMaxValue(): Int {
        TODO()
    }

    private suspend fun getValue(date: LocalDate): Int {
        return eventRepository.getEventInstancesCount(eventId, date)
    }

    class Factory @Inject constructor(
        private val eventRepository: EventRepository
    ) {
        fun createEventInstanceCountDayValueProvider(eventId: String): EventInstanceCountDayValueProvider {
            return EventInstanceCountDayValueProvider(eventId, eventRepository)
        }
    }
}