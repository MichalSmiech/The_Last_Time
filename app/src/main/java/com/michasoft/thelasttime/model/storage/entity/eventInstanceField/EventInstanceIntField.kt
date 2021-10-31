package com.michasoft.thelasttime.model.storage.entity.eventInstanceField

import androidx.room.Entity
import com.michasoft.thelasttime.model.eventInstanceField.IntField
import com.michasoft.thelasttime.model.eventInstanceField.TextField

/**
 * Created by mśmiech on 31.10.2021.
 */
@Entity(tableName = EventInstanceIntFieldEntity.TABLE_NAME, primaryKeys = ["instanceId", "fieldSchemaId"])
class EventInstanceIntFieldEntity(
    val instanceId: Long,
    val fieldSchemaId: Long,
    val value: Int?
) {
    constructor(eventId: Long, field: IntField): this(eventId, field.fieldSchemaId, field.value)

    fun toModel() = IntField(fieldSchemaId, value)

    companion object {
        const val TABLE_NAME = "EventInstanceIntFields"
    }
}