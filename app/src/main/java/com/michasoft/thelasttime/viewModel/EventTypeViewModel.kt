package com.michasoft.thelasttime.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventType
import com.michasoft.thelasttime.model.repo.IEventsRepository
import com.michasoft.thelasttime.util.FlowEvent
import com.michasoft.thelasttime.util.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by mśmiech on 08.07.2021.
 */
class EventTypeViewModel @Inject constructor(
    private val eventsRepository: IEventsRepository
) : CommonViewModel() {
    private var eventTypeId: Long? = null
    private var originalEventType: EventType? = null
    val name = MutableLiveData<String>("")
    var events = MutableLiveData<List<Event>>(emptyList())

    fun start(eventTypeId: Long) {
        viewModelScope.launch {
            this@EventTypeViewModel.eventTypeId = eventTypeId
            val eventType = eventsRepository.getEventType(eventTypeId)
            originalEventType = eventType
            name.value = eventType.name
            events.value = eventsRepository.getEvents(eventTypeId)
        }
    }

    fun showEvent(event: Event) {
        flowEventBus.value = ShowEvent(event.id)
    }

    class ShowEvent(val eventId: Long): FlowEvent()
}