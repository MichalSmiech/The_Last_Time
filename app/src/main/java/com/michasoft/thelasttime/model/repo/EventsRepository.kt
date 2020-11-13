package com.michasoft.thelasttime.model.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.michasoft.thelasttime.model.Event

/**
 * Created by mśmiech on 11.11.2020.
 */
class EventsRepository {
    val

    fun getEventsLive(): LiveData<List<Event>> {
        return MutableLiveData()
    }
}