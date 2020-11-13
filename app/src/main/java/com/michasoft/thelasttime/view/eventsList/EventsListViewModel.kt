package com.michasoft.thelasttime.view.eventsList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.michasoft.thelasttime.model.Event

class EventsListViewModel : ViewModel() {
    var events = MutableLiveData<List<Event>>(sampleEvents())

    fun sampleEvents(): List<Event> {
        val list = ArrayList<Event>()
        for (i in (0..10)) {
            val event = Event()
            event.id = i.toLong()
            event.name = "Event_$i"
            list.add(event)
        }
        return list
    }
}