package com.michasoft.thelasttime.model

import org.joda.time.DateTime

/**
 * Created by mśmiech on 02.05.2021.
 */
open class Event(
    var id: String,
    var displayName: String,
    val createTimestamp: DateTime
) {
    open var lastInstanceTimestamp: DateTime? = null
    var eventInstanceSchema: EventInstanceSchema? = null
}