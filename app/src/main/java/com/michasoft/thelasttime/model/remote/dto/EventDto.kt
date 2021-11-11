package com.michasoft.thelasttime.model.remote.dto

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import com.michasoft.thelasttime.model.Event

/**
 * Created by mśmiech on 01.11.2021.
 */
class EventDto(
    var displayName: String? = null
) {
    constructor(event: Event): this(event.displayName)

    fun toModel(id: String) = Event(id, displayName!!)
}