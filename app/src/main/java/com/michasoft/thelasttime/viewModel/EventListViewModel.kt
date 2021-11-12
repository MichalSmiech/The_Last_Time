package com.michasoft.thelasttime.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.repo.IEventRepository
import com.michasoft.thelasttime.util.FlowEvent
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import javax.inject.Inject

/**
 * Created by mśmiech on 02.05.2021.
 */
class EventListViewModel @Inject constructor(
    private val eventRepository: IEventRepository
): CommonViewModel() {
    var events = MutableLiveData<List<Event>>(emptyList())

    init {
        viewModelScope.launch {
            events.value = eventRepository.getEvents()

        }
    }

    fun createNewEventType() {
        flowEventBus.value = CreateNewEvent()
    }

    fun refreshData() {
        viewModelScope.launch {
            events.value = eventRepository.getEvents()

        }
    }

    fun quickAddEventInstance(event: Event) {
        events.value?.find { it.id == event.id }?.let {
            it.lastEventTimestamp = DateTime.now()
        }?.also {
            events.postValue(events.value)
        }
    }

    fun showEventType(event: Event) {
        flowEventBus.value = ShowEvent(event.id)
    }

    fun addEvent(event: Event) {
        //TODO
    }

    class CreateNewEvent : FlowEvent()
    class ShowEvent(val eventTypeId: String) : FlowEvent()
}