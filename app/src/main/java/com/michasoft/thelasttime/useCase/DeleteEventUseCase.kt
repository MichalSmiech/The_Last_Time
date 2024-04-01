package com.michasoft.thelasttime.useCase

import com.michasoft.thelasttime.repo.EventRepository
import com.michasoft.thelasttime.repo.LabelRepository
import javax.inject.Inject

class DeleteEventUseCase @Inject constructor(
    private val eventRepository: EventRepository,
    private val labelRepository: LabelRepository
) {
    suspend fun execute(eventId: String) {
        labelRepository.deleteEventAllLabels(eventId)
        eventRepository.deleteEvent(eventId)
    }
}