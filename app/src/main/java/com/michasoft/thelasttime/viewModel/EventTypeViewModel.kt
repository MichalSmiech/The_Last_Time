package com.michasoft.thelasttime.viewModel

import androidx.lifecycle.MutableLiveData
import com.michasoft.thelasttime.model.EventType
import com.michasoft.thelasttime.model.repo.IEventsRepository
import com.michasoft.thelasttime.util.FlowEvent
import com.michasoft.thelasttime.util.SingleLiveEvent
import javax.inject.Inject

/**
 * Created by mśmiech on 08.07.2021.
 */
class EventTypeViewModel @Inject constructor(
    private val eventsRepository: IEventsRepository
): CommonViewModel() {
    private var eventTypeId: Long? = null
    private var originalEventType: EventType? = null
    val name = MutableLiveData<String>("")

    fun start(eventTypeId: Long) {
        this.eventTypeId = eventTypeId
        val eventType = eventsRepository.getEventType(eventTypeId)
        originalEventType = eventType
        name.value = eventType.name
    }
}