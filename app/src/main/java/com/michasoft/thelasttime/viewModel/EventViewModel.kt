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
class EventViewModel @Inject constructor(
    private val eventRepository: IEventRepository
) : CommonViewModel() {
    private var eventId: String? = null
    private var originalEvent: Event? = null
    val name = MutableLiveData("")
    var instances = MutableLiveData<List<EventInstance>>(emptyList())

    fun start(eventId: String) {
        this@EventViewModel.eventId = eventId
        viewModelScope.launch {
            val event = eventRepository.getEvent(eventId)!!
            originalEvent = event
            name.value = event.displayName
            instances.value = eventRepository.getEventInstances(eventId)
        }
    }

    fun createEventInstance() {
        flowEventBus.value = ShowAddEventInstanceBottomSheet(eventId!!)
    }

    fun showEvent(instance: EventInstance) {
        flowEventBus.value = ShowEventInstance(instance.eventId, instance.id)
    }

    private fun buildEvent(): Event {
        return originalEvent!!.copy(displayName = name.value!!)
    }

    suspend fun saveIfNeeded() {
        val event = buildEvent()
        if(event != originalEvent) {
            saveEvent(event)
        }
    }

    private suspend fun saveEvent(event: Event) {
        eventRepository.update(event)
    }

    class ShowEventInstance(val eventId: String, val instanceId: String): FlowEvent()
    class ShowAddEventInstanceBottomSheet(val eventId: String): FlowEvent()
}