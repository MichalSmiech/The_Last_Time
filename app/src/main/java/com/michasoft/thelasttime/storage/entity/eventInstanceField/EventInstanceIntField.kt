package com.michasoft.thelasttime.storage.entity.eventInstanceField

import androidx.room.Entity
import com.michasoft.thelasttime.model.EventInstanceFieldSchema
import com.michasoft.thelasttime.model.eventInstanceField.IntField

/**
 * Created by mśmiech on 31.10.2021.
 */
@Entity(tableName = EventInstanceIntFieldEntity.TABLE_NAME, primaryKeys = ["instanceId", "fieldSchemaId"])
class EventInstanceIntFieldEntity(
    val instanceId: String,
    val fieldSchemaId: String,
    val value: Int?
) {
    constructor(eventId: String, field: IntField): this(eventId, field.fieldSchema.id, field.value)

    fun toModel(fieldSchema: EventInstanceFieldSchema) = IntField(fieldSchema, value)

    companion object {
        const val TABLE_NAME = "EventInstanceIntFields"
    }
}