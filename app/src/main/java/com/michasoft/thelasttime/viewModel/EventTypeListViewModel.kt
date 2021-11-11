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
class EventTypeListViewModel @Inject constructor(
    private val eventRepository: IEventRepository
): CommonViewModel() {
    var eventTypes = MutableLiveData<List<Event>>(emptyList())

    fun createNewEventType() {
        flowEventBus.value = CreateNewEventType()
    }

    fun refreshData() {
        viewModelScope.launch {
            eventTypes.value = eventRepository.getEvents()

        }
    }

    fun quickAddEvent(event: Event) {
        eventTypes.value?.find { it.id == event.id }?.let {
            it.lastEventTimestamp = DateTime.now()
        }?.also {
            eventTypes.postValue(eventTypes.value)
        }
    }

    fun showEventType(event: Event) {
        flowEventBus.value = ShowEventType(event.id)
    }

    fun addEvent(event: Event) {
        //TODO
    }

    class CreateNewEventType : FlowEvent()
    class ShowEventType(val eventTypeId: String) : FlowEvent()
}