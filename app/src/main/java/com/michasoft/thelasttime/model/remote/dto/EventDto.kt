package com.michasoft.thelasttime.model.remote.dto

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import com.michasoft.thelasttime.model.Event
import org.joda.time.DateTime
import java.util.*

/**
 * Created by mśmiech on 01.11.2021.
 */
class EventDto(
    var displayName: String? = null,
    var createTimestamp: Date? = null
) {
    constructor(event: Event): this(event.displayName, event.createTimestamp.toDate())

    fun toModel(id: String) = Event(id, displayName!!, DateTime(createTimestamp))
}