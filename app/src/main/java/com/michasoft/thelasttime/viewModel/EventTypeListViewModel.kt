package com.michasoft.thelasttime.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.michasoft.thelasttime.model.EventType
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
    var eventTypes = MutableLiveData<List<EventType>>(emptyList())

    fun createNewEventType() {
        flowEventBus.value = CreateNewEventType()
    }

    fun refreshData() {
        viewModelScope.launch {
            eventTypes.value = eventRepository.getEventTypes()

        }
    }

    fun quickAddEvent(eventType: EventType) {
        eventTypes.value?.find { it.id == eventType.id }?.let {
            it.lastEventTimestamp = DateTime.now()
        }?.also {
            eventTypes.postValue(eventTypes.value)
        }
    }

    fun showEventType(eventType: EventType) {
        flowEventBus.value = ShowEventType(eventType.id)
    }

    fun addEvent(eventType: EventType) {
        //TODO
    }

    class CreateNewEventType : FlowEvent()
    class ShowEventType(val eventTypeId: Long) : FlowEvent()
}