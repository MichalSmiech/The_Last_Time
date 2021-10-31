package com.michasoft.thelasttime.model

import org.joda.time.DateTime

/**
 * Created by mśmiech on 08.07.2021.
 */
data class EventInstance(
    var id: Long,
    val eventId: Long,
    val timestamp: DateTime,
    val fields: ArrayList<EventInstanceField>
)