package com.michasoft.thelasttime.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.michasoft.thelasttime.model.EventType
import org.joda.time.DateTime

/**
 * Created by mśmiech on 08.05.2021.
 */

class EditEventTypeViewModel: ViewModel() {
    var eventTypeId = 0L
    val eventTypeName = MutableLiveData<String>("")


    fun save() {
        val eventType = EventType(eventTypeId, eventTypeName.value!!, DateTime.now())
    }
}