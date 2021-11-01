package com.michasoft.thelasttime.model.remote.dto

import com.michasoft.thelasttime.model.EventInstanceField
import com.michasoft.thelasttime.model.EventInstanceFieldSchema

/**
 * Created by mśmiech on 01.11.2021.
 */
class EventInstanceFieldSchemaDto(
    val order: Int,
    val type: EventInstanceField.Type,
    val displayTitle: String
) {
    constructor(eventInstanceFieldSchema: EventInstanceFieldSchema) : this(
        eventInstanceFieldSchema.order,
        eventInstanceFieldSchema.type,
        eventInstanceFieldSchema.displayTitle
    )

    fun toModel(id: Long) = EventInstanceFieldSchema(id, order, type, displayTitle)
}