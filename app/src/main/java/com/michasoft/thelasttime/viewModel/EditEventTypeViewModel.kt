package com.michasoft.thelasttime.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.michasoft.thelasttime.model.EventType
import com.michasoft.thelasttime.model.repo.IEventsRepository
import com.michasoft.thelasttime.util.FlowEvent
import com.michasoft.thelasttime.util.SingleLiveEvent
import org.joda.time.DateTime
import javax.inject.Inject

/**
 * Created by mśmiech on 08.05.2021.
 */

class EditEventTypeViewModel @Inject constructor(
    private val eventsRepository: IEventsRepository
): ViewModel() {
    var flowEventBus = SingleLiveEvent<FlowEvent>()
    private var eventTypeId: Long? = null
    private var originalEventType: EventType? = null
    val name = MutableLiveData<String>("")

    fun save() {
        originalEventType?.name = name.value!!
        eventsRepository.save(originalEventType!!)
        flowEventBus.value = Finish()
    }

    fun start(eventTypeId: Long) {
        this.eventTypeId = eventTypeId
        val eventType = eventsRepository.getEventType(eventTypeId)
        originalEventType = eventType
        name.value = eventType.name
    }

    class Finish : FlowEvent()
}