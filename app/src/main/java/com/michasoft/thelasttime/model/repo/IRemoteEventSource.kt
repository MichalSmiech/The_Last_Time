package com.michasoft.thelasttime.model.repo

import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.EventInstanceSchema

/**
 * Created by mśmiech on 01.11.2021.
 */
interface IRemoteEventSource {
    suspend fun insertEvent(event: Event): Long
    suspend fun getEvent(eventId: Long): Event?
    suspend fun deleteEvent(eventId: Long)

    suspend fun getEventInstanceSchema(eventId: Long): EventInstanceSchema

    suspend fun insertEventInstance(instance: EventInstance): Long
    suspend fun getEventInstance(eventId: Long, instanceSchema: EventInstanceSchema, instanceId: Long): EventInstance?
    suspend fun deleteEventInstance(instance: EventInstance)
}