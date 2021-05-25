package com.michasoft.thelasttime.model

import org.joda.time.DateTime

/**
 * Created by mśmiech on 02.05.2021.
 */
data class EventType(var id: Long, var name: String, var lastEventTimestamp: DateTime?)