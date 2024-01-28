package com.michasoft.thelasttime.model.dto

import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstanceSchema
import org.joda.time.DateTime
import java.util.Date

/**
 * Created by mśmiech on 01.11.2021.
 */
class EventDto(
    var displayName: String? = null,
    var createTimestamp: Date? = null
) {
    constructor(event: Event) : this(event.name, event.createTimestamp.toDate())

    fun toModel(id: String, eventInstanceSchema: EventInstanceSchema) = Event(id, displayName!!, DateTime(createTimestamp), eventInstanceSchema)
}