package com.michasoft.thelasttime.model.repo

import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstanceScheme

/**
 * Created by mśmiech on 05.09.2021.
 */
interface IEventSource {
    suspend fun getEvent(eventId: Long): Event?
    suspend fun getEventInstanceScheme(eventId: Long): EventInstanceScheme
    suspend fun getEventInstance(instanceId: Long): EventInstance?
    suspend fun insertEvent(event: Event): Long
    suspend fun insertEventInstance(instance: EventInstance): Long
}