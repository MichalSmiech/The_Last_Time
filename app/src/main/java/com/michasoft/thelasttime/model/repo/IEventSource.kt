package com.michasoft.thelasttime.model.repo

import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstanceScheme

/**
 * Created by mśmiech on 05.09.2021.
 */
interface IEventSource {
    suspend fun insertEvent(event: Event): Long
    suspend fun getEvent(eventId: Long): Event?
    suspend fun deleteEvent(event: Event)

    suspend fun getEventInstanceScheme(eventId: Long): EventInstanceScheme

    suspend fun insertEventInstance(instance: EventInstance): Long
    suspend fun getEventInstance(instanceId: Long): EventInstance?
    suspend fun deleteEventInstance(instance: EventInstance)
}