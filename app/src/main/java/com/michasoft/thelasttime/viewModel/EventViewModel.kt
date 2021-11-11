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

class EventViewModel @Inject constructor(
    private val eventRepository: IEventRepository
) : CommonViewModel() {
    private var eventId: String? = null
    private var originalEventInstance: EventInstance? = null
    private var event: Event? = null
    val timestamp = MutableLiveData<DateTime>()
    val timestampString = Transformations.map<DateTime, String>(timestamp) {
        return@map it.toString("dd.MM.yyyy HH:mm")
    }
    val eventTypeName = MutableLiveData("")

    fun setEventId(eventId: String) {
        viewModelScope.launch {
            this@EventViewModel.eventId = eventId
            val event = eventRepository.getEventInstance("1", eventId)!!
            originalEventInstance = event
            timestamp.value = event.timestamp
            this@EventViewModel.event = eventRepository.getEvent(event.eventId)
            eventTypeName.value = this@EventViewModel.event!!.displayName
        }
    }

    fun showDatePicker() {
        flowEventBus.value = ShowDatePicker(timestamp.value!!)
    }

    class ShowDatePicker(val timestamp: DateTime): FlowEvent()
}