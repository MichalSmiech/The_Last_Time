package com.michasoft.thelasttime.eventDetails

import com.michasoft.thelasttime.calendarWidget.githubWidget.DateValueProvider
import com.michasoft.thelasttime.model.DateRange
import com.michasoft.thelasttime.repo.EventRepository
import org.joda.time.LocalDate
import javax.inject.Inject

class EventInstanceCountDateValueProvider private constructor(
    private val eventId: String,
    private val dateRange: DateRange,
    private val eventRepository: EventRepository
) : DateValueProvider {
    private var maxValue: Int = 0

    private suspend fun initMaxValue() {
        maxValue = eventRepository.getMaxEventInstancesCountInDateRange(eventId, dateRange)
    }

    override suspend fun getNormalizeValue(date: LocalDate): Float {
        if (maxValue == 0) {
            return 0f
        }
        return (getValue(date).toFloat() / maxValue).coerceAtMost(1f)
    }

    private suspend fun getValue(date: LocalDate): Int {
        return eventRepository.getEventInstancesCount(eventId, date)
    }

    class Factory @Inject constructor(
        private val eventRepository: EventRepository,
    ) {
        suspend fun create(eventId: String, dateRange: DateRange): EventInstanceCountDateValueProvider {
            return EventInstanceCountDateValueProvider(eventId, dateRange, eventRepository).apply { initMaxValue() }
        }
    }
}