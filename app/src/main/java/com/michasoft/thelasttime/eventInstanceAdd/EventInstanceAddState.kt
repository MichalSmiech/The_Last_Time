package com.michasoft.thelasttime.eventInstanceAdd

import com.michasoft.thelasttime.model.EventInstance

/**
 * Created by mśmiech on 27.09.2023.
 */
data class EventInstanceAddState(
    val instance: EventInstance?,
    val eventName: String
)