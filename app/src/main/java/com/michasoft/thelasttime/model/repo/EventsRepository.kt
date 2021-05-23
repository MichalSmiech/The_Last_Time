package com.michasoft.thelasttime.model.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.michasoft.thelasttime.model.EventType
import org.joda.time.DateTime

/**
 * Created by mśmiech on 11.11.2020.
 */
class EventsRepository: IEventsRepository {
    override fun getEvents(): ArrayList<EventType> {
        val list = ArrayList<EventType>()
        list.add(EventType(1L, "Plants", DateTime.now()))
        list.add(EventType(2L, "Vacuum", DateTime.now().minusDays(3)))
        return list
    }

//    fun getEventsLive(): LiveData<List<Event>> {
//        return MutableLiveData()
//    }
}