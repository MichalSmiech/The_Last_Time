package com.michasoft.thelasttime.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.repo.IEventRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by mśmiech on 08.05.2021.
 */

class EditEventTypeViewModel @Inject constructor(
    private val eventRepository: IEventRepository
): CommonViewModel() {
    private var eventTypeId: Long? = null
    private var originalEvent: Event? = null
    val name = MutableLiveData<String>("")

    fun save() {
        originalEvent?.displayName = name.value!!
        eventRepository.save(originalEvent!!)
        flowEventBus.value = Finish()
    }

    fun start(eventTypeId: Long) {
        viewModelScope.launch {
            this@EditEventTypeViewModel.eventTypeId = eventTypeId
            val eventType = eventRepository.getEventType(eventTypeId)
            originalEvent = eventType
            name.value = eventType.displayName
        }
    }
}