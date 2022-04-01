package com.michasoft.thelasttime.model.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.michasoft.thelasttime.model.EventInstanceField
import com.michasoft.thelasttime.model.EventInstanceFieldSchema

/**
 * Created by mśmiech on 31.10.2021.
 */
@Entity(tableName = EventInstanceFieldSchemaEntity.TABLE_NAME)
class EventInstanceFieldSchemaEntity(
    @PrimaryKey
    var id: String,
    val eventId: String,
    val order: Int,
    val type: EventInstanceField.Type,
    val displayTitle: String,
) {
    constructor(eventId: String, schema: EventInstanceFieldSchema): this(schema.id, eventId, schema.order, schema.type, schema.displayTitle)

    fun toModel() = EventInstanceFieldSchema(id, order, type, displayTitle)

    companion object {
        const val TABLE_NAME = "EventInstanceFieldSchemas"
    }
}