package com.michasoft.thelasttime.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.michasoft.thelasttime.model.EventType
import com.michasoft.thelasttime.model.repo.IEventsRepository
import com.michasoft.thelasttime.util.FlowEvent
import com.michasoft.thelasttime.util.SingleLiveEvent
import org.intellij.lang.annotations.Flow
import javax.inject.Inject

/**
 * Created by mśmiech on 02.05.2021.
 */
class EventTypeListViewModel @Inject constructor(
    private val eventsRepository: IEventsRepository
): ViewModel() {
    var flowEventBus = SingleLiveEvent<FlowEvent>()
    var eventTypes = MutableLiveData<List<EventType>>(eventsRepository.getEventTypes())

    fun createNewEventType() {
        flowEventBus.value = CreateNewEventType()
    }

    fun refreshData() {
        eventTypes.value = eventsRepository.getEventTypes()
    }

    class CreateNewEventType : FlowEvent()
}