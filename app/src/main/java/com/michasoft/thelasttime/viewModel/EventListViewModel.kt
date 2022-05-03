package com.michasoft.thelasttime.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.repo.IEventRepository
import com.michasoft.thelasttime.util.EventInstanceFactory
import com.michasoft.thelasttime.util.FlowEvent
import com.michasoft.thelasttime.util.ListObserver
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by mśmiech on 02.05.2021.
 */
class EventListViewModel(
    private val eventRepository: IEventRepository
): CommonViewModel() {
    var events = MutableLiveData<List<Event>>(emptyList())
    var eventsObserver: ListObserver<Event>? = null

    init {
        viewModelScope.launch {
            events.value = eventRepository.getEventsWithLastInstanceTimestamp()
        }
    }

    fun createNewEventType() {
        flowEventBus.value = CreateNewEvent()
    }

    fun refreshData() {
        viewModelScope.launch {
            events.value = eventRepository.getEventsWithLastInstanceTimestamp()
        }
    }

    fun showEventType(event: Event) {
        flowEventBus.value = ShowEvent(event.id)
    }

    fun addEventInstance(event: Event) {
        flowEventBus.value = ShowAddEventInstanceBottomSheet(event.id)
    }

    class CreateNewEvent : FlowEvent()
    class ShowEvent(val eventTypeId: String) : FlowEvent()
    class ShowAddEventInstanceBottomSheet(val eventId: String): FlowEvent()

    @Suppress("UNCHECKED_CAST")
    class Factory @Inject constructor(
        private val eventRepository: IEventRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EventListViewModel::class.java)) {
                return EventListViewModel(eventRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}