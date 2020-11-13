package com.michasoft.thelasttime.model

import org.joda.time.DateTime

/**
 * Created by mśmiech on 31.10.2020.
 */
class EventInstance {
    private var id: Long? = null
    private var timestamp: DateTime? = null
    private var displayName: String? = null
    private var additionalProperties: List<EventPropertyInstance>? = null
}