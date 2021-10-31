package com.michasoft.thelasttime.model.storage.entity.eventInstanceField

import androidx.room.Entity
import com.michasoft.thelasttime.model.eventInstanceField.TextField

/**
 * Created by mśmiech on 31.10.2021.
 */

@Entity(tableName = EventInstanceTextFieldEntity.TABLE_NAME, primaryKeys = ["instanceId", "fieldSchemaId"])
class EventInstanceTextFieldEntity(
    val instanceId: Long,
    val fieldSchemaId: Long,
    val value: String?
) {
    constructor(eventId: Long, field: TextField): this(eventId, field.fieldSchemaId, field.value)

    fun toModel() = TextField(fieldSchemaId, value)

    companion object {
        const val TABLE_NAME = "EventInstanceTextFields"
    }
}