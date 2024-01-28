package com.michasoft.thelasttime.model.dto

import com.michasoft.thelasttime.model.EventInstanceField
import com.michasoft.thelasttime.model.EventInstanceFieldSchema

/**
 * Created by mśmiech on 01.11.2021.
 */
class EventInstanceFieldSchemaDto(
    val order: Int? = null,
    val type: EventInstanceField.Type? = null,
    val displayTitle: String? = null
) {
    constructor(eventInstanceFieldSchema: EventInstanceFieldSchema) : this(
        eventInstanceFieldSchema.order,
        eventInstanceFieldSchema.type,
        eventInstanceFieldSchema.displayTitle
    )

    fun toModel(id: String) = EventInstanceFieldSchema(id, order!!, type!!, displayTitle!!)
}