package com.michasoft.thelasttime.model.repo

import com.michasoft.thelasttime.model.EventType

/**
 * Created by mśmiech on 02.05.2021.
 */
interface IEventsRepository {
    fun getEvents(): ArrayList<EventType>

}