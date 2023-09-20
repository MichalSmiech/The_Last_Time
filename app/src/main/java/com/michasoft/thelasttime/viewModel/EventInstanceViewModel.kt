package com.michasoft.thelasttime.viewModel

import androidx.lifecycle.*
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.repo.EventRepository
import com.michasoft.thelasttime.util.FlowEvent
import com.michasoft.thelasttime.util.ShowDeleteConfirmationDialog
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import javax.inject.Inject

class EventInstanceViewModel(
    private val eventRepository: EventRepository
) : CommonViewModel() {
    private var eventId: String? = null
    private var instanceId: String? = null
    private var originalEventInstance: EventInstance? = null
    private var event = MutableLiveData<Event>()
    var eventName = event.map { it.displayName }
    val timestamp = MutableLiveData(DateTime.now())
    val timestampDateString = timestamp.map {
        return@map it.toString("E, dd MMM yyyy")
    }
    val timestampTimeString = timestamp.map {
        return@map it.toString("HH:mm")
    }

    fun setInitData(eventId: String, instanceId: String? = null) {
        this@EventInstanceViewModel.eventId = eventId
        if(instanceId != null) {
            this@EventInstanceViewModel.instanceId = instanceId
            viewModelScope.launch {
                val instance = eventRepository.getEventInstance(eventId, instanceId)!!
                originalEventInstance = instance
                timestamp.value = instance.timestamp
                this@EventInstanceViewModel.event.value = eventRepository.getEvent(instance.eventId)
            }
        } else {
            viewModelScope.launch {
                originalEventInstance = eventRepository.createEventInstance(eventId)
                timestamp.value = originalEventInstance!!.timestamp
                this@EventInstanceViewModel.event.value = eventRepository.getEvent(eventId)
            }
        }
    }

    fun showDatePicker() {
        flowEventBus.value = ShowDatePicker(timestamp.value!!)
    }

    fun showTimePicker() {
        flowEventBus.value = ShowTimePicker(timestamp.value!!)
    }

    fun save() {
        viewModelScope.launch {
            val instance = buildInstance()
            eventRepository.insert(instance)
            flowEventBus.value = Finish()
        }
    }

    private fun buildInstance(): EventInstance {
        val instance = originalEventInstance!!.copy(
            timestamp = timestamp.value!!
        )
        return instance
    }

    suspend fun saveIfNeeded() {
        val instance = buildInstance()
        if (instance != originalEventInstance) {
            saveInstance(instance)
        }
    }

    private suspend fun saveInstance(instance: EventInstance = buildInstance()) {
        eventRepository.update(instance)
    }

    fun delete(needConfirmation: Boolean = false) {
        if (needConfirmation) {
            flowEventBus.value = ShowDeleteConfirmationDialog()
            return
        }
        viewModelScope.launch {
            eventRepository.deleteEventInstance(eventId!!, instanceId!!)
            flowEventBus.value = Finish()
        }
    }

    class ShowDatePicker(val timestamp: DateTime): FlowEvent()
    class ShowTimePicker(val timestamp: DateTime): FlowEvent()

    @Suppress("UNCHECKED_CAST")
    class Factory @Inject constructor(
        private val eventRepository: EventRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EventInstanceViewModel::class.java)) {
                return EventInstanceViewModel(eventRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}