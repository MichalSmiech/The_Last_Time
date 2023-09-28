package com.michasoft.thelasttime.util

import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstanceSchema
import org.joda.time.DateTime

/**
 * Created by mśmiech on 28.09.2023.
 */
object EventFactory {
    fun createEvent(name: String): Event {
        return Event(
            id = IdGenerator.newId(),
            name = name,
            createTimestamp = DateTime.now(),
            eventInstanceSchema = EventInstanceSchema()
        )
    }
}