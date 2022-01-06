package com.michasoft.thelasttime.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.repo.IEventRepository
import com.michasoft.thelasttime.util.FlowEvent
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import javax.inject.Inject

class EventInstanceViewModel @Inject constructor(
    private val eventRepository: IEventRepository
) : CommonViewModel() {
    private var eventId: String? = null
    private var originalEventInstance: EventInstance? = null
    private var event: Event? = null
    val timestamp = MutableLiveData<DateTime>()
    val timestampDateString = Transformations.map(timestamp) {
        return@map it.toString("E, dd MMM yyyy")
    }
    val timestampTimeString = Transformations.map(timestamp) {
        return@map it.toString("HH:mm")
    }

    fun setEventId(eventId: String) {
        viewModelScope.launch {
            this@EventInstanceViewModel.eventId = eventId
            val event = eventRepository.getEventInstance("1", eventId)!!
            originalEventInstance = event
            timestamp.value = event.timestamp
            this@EventInstanceViewModel.event = eventRepository.getEvent(event.eventId)
        }
    }

    fun showDatePicker() {
        flowEventBus.value = ShowDatePicker(timestamp.value!!)
    }

    fun showTimePicker() {
        flowEventBus.value = ShowTimePicker(timestamp.value!!)
    }

    class ShowDatePicker(val timestamp: DateTime): FlowEvent()
    class ShowTimePicker(val timestamp: DateTime): FlowEvent()
}