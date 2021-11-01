package com.michasoft.thelasttime.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.repo.IEventRepository
import com.michasoft.thelasttime.util.FlowEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by mśmiech on 08.07.2021.
 */
class EventTypeViewModel @Inject constructor(
    private val eventRepository: IEventRepository
) : CommonViewModel() {
    private var eventTypeId: Long? = null
    private var originalEvent: Event? = null
    val name = MutableLiveData<String>("")
    var events = MutableLiveData<List<EventInstance>>(emptyList())

    fun start(eventTypeId: Long) {
        viewModelScope.launch {
            this@EventTypeViewModel.eventTypeId = eventTypeId
            val eventType = eventRepository.getEvent(eventTypeId)!!
            originalEvent = eventType
            name.value = eventType.displayName
            events.value = eventRepository.getEventInstances(eventTypeId)
        }
    }

    fun showEvent(eventInstance: EventInstance) {
        flowEventBus.value = ShowEvent(eventInstance.id)
    }

    class ShowEvent(val eventId: Long): FlowEvent()
}