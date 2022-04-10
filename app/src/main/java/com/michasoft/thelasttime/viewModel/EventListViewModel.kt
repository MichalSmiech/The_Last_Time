package com.michasoft.thelasttime.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.repo.IEventRepository
import com.michasoft.thelasttime.util.EventInstanceFactory
import com.michasoft.thelasttime.util.FlowEvent
import com.michasoft.thelasttime.util.ListObserver
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by mśmiech on 02.05.2021.
 */
class EventListViewModel @Inject constructor(
    private val eventRepository: IEventRepository
): CommonViewModel() {
    var events = MutableLiveData<List<Event>>(emptyList())
    var eventsObserver: ListObserver<Event>? = null

    init {
        viewModelScope.launch {
            events.value = eventRepository.getEventsWithLastInstanceTimestamp()
        }
    }

    fun createNewEventType() {
        flowEventBus.value = CreateNewEvent()
    }

    fun refreshData() {
        viewModelScope.launch {
            events.value = eventRepository.getEventsWithLastInstanceTimestamp()
        }
    }

    fun quickAddEventInstance(event: Event) {
        viewModelScope.launch {
            val instance = EventInstanceFactory.createEmptyEventInstance(event)
            eventRepository.insert(instance)
            event.lastInstanceTimestamp = instance.timestamp
            eventsObserver?.onChanged(event)
        }
    }

    fun showEventType(event: Event) {
        flowEventBus.value = ShowEvent(event.id)
    }

    fun addEventInstance(event: Event) {
        flowEventBus.value = ShowAddEventInstanceBottomSheet(event.id)
    }

    class CreateNewEvent : FlowEvent()
    class ShowEvent(val eventTypeId: String) : FlowEvent()
    class ShowAddEventInstanceBottomSheet(val eventId: String): FlowEvent()
}