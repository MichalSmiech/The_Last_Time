package com.michasoft.thelasttime.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstanceSchema
import com.michasoft.thelasttime.model.repo.IEventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import javax.inject.Inject

/**
 * Created by mśmiech on 08.05.2021.
 */

class EditEventViewModel @Inject constructor(
    private val eventRepository: IEventRepository
) : CommonViewModel() {
    private lateinit var eventId: String
    private var originalEvent: Event? = null
    val name = MutableLiveData("")

    fun save() {
        viewModelScope.launch {
            val originalEvent = this@EditEventViewModel.originalEvent
            if (originalEvent == null) {
                val newEvent = Event(
                    eventId,
                    name.value!!,
                    DateTime.now()
                )
                newEvent.eventInstanceSchema = EventInstanceSchema()
                eventRepository.insertEvent(newEvent)
            } else {
                originalEvent.displayName = name.value!!
//              TODO  eventRepository.updateEvent(originalEvent)
            }
            //TODO upewnić się czy trzeba to na MAIN?
            withContext(Dispatchers.Main) {
                flowEventBus.value = Finish()
            }
        }
    }

    fun start(eventId: String) {
        this@EditEventViewModel.eventId = eventId
        viewModelScope.launch {
            val event = eventRepository.getEvent(eventId)
            originalEvent = event
            name.value = event?.displayName ?: ""
        }
    }
}