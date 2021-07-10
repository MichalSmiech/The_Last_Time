package com.michasoft.thelasttime.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventType
import com.michasoft.thelasttime.model.repo.IEventsRepository
import com.michasoft.thelasttime.util.FlowEvent
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import javax.inject.Inject

class EventViewModel @Inject constructor(
    private val eventsRepository: IEventsRepository
) : CommonViewModel() {
    private var eventId: Long? = null
    private var originalEvent: Event? = null
    private var eventType: EventType? = null
    val timestamp = MutableLiveData<DateTime>()
    val timestampString = Transformations.map<DateTime, String>(timestamp) {
        return@map it.toString("dd.MM.yyyy HH:mm")
    }
    val eventTypeName = MutableLiveData("")

    fun setEventId(eventId: Long) {
        viewModelScope.launch {
            this@EventViewModel.eventId = eventId
            val event = eventsRepository.getEvent(eventId)
            originalEvent = event
            timestamp.value = event.timestamp
            eventType = eventsRepository.getEventType(event.eventTypeId)
            eventTypeName.value = eventType!!.name
        }
    }

    fun showDatePicker() {
        flowEventBus.value = ShowDatePicker(timestamp.value!!)
    }

    class ShowDatePicker(val timestamp: DateTime): FlowEvent()
}