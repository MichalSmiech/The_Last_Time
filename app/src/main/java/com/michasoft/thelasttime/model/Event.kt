package com.michasoft.thelasttime.model

import org.joda.time.DateTime

/**
 * Created by mśmiech on 02.05.2021.
 */
data class Event(
    var id: String,
    var displayName: String
) {
    var lastEventTimestamp: DateTime? = null
    var eventInstanceSchema: EventInstanceSchema? = null
}