package com.michasoft.thelasttime.model.storage.entity.eventInstanceField

import androidx.room.Entity
import com.michasoft.thelasttime.model.eventInstanceField.DoubleField

/**
 * Created by mśmiech on 31.10.2021.
 */
@Entity(tableName = EventInstanceDoubleFieldEntity.TABLE_NAME, primaryKeys = ["instanceId", "fieldSchemaId"])
class EventInstanceDoubleFieldEntity(
    val instanceId: String,
    val fieldSchemaId: String,
    val value: Double?
) {
    constructor(eventId: String, field: DoubleField): this(eventId, field.fieldSchemaId, field.value)

    fun toModel() = DoubleField(fieldSchemaId, value)

    companion object {
        const val TABLE_NAME = "EventInstanceDoubleFields"
    }
}