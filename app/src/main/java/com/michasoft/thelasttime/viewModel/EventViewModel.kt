package com.michasoft.thelasttime.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.repo.IEventRepository
import com.michasoft.thelasttime.util.FlowEvent
import com.michasoft.thelasttime.util.ShowDeleteConfirmationDialog
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by mśmiech on 08.07.2021.
 */
class EventViewModel(
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
        if (event != originalEvent) {
            saveEvent(event)
        }
    }

    private suspend fun saveEvent(event: Event) {
        eventRepository.update(event)
    }

    suspend fun deleteEvent(needConfirmation: Boolean = false) {
        if (needConfirmation) {
            flowEventBus.postValue(ShowDeleteConfirmationDialog())
            return
        }
        eventId?.let { eventRepository.deleteEvent(it) }
            ?: Timber.e("eventId is null while call deleteEvent function")
        flowEventBus.postValue(Finish())
    }

    class ShowEventInstance(val eventId: String, val instanceId: String) : FlowEvent()
    class ShowAddEventInstanceBottomSheet(val eventId: String) : FlowEvent()

    @Suppress("UNCHECKED_CAST")
    class Factory @Inject constructor(
        private val eventRepository: IEventRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
                return EventViewModel(eventRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}