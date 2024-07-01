package com.michasoft.thelasttime.eventDetails

import com.michasoft.thelasttime.repo.EventRepository
import com.michasoft.thelasttime.repo.LabelRepository
import com.michasoft.thelasttime.repo.ReminderRepository
import javax.inject.Inject

class DeleteEventUseCase @Inject constructor(
    private val eventRepository: EventRepository,
    private val labelRepository: LabelRepository,
    private val reminderRepository: ReminderRepository
) {
    suspend fun execute(eventId: String) {
        labelRepository.deleteEventAllLabels(eventId)
        reminderRepository.deleteEventReminders(eventId)
        eventRepository.deleteEvent(eventId)
    }
}