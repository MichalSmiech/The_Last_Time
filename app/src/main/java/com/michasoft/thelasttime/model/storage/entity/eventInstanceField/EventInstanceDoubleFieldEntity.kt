package com.michasoft.thelasttime.model.storage.entity.eventInstanceField

import androidx.room.Entity
import com.michasoft.thelasttime.model.eventInstanceField.DoubleField

/**
 * Created by mśmiech on 31.10.2021.
 */
@Entity(tableName = EventInstanceDoubleFieldEntity.TABLE_NAME)
class EventInstanceDoubleFieldEntity(
    val instanceId: Long,
    val fieldSchemaId: Long,
    val value: Double?
) {
    fun toModel() = DoubleField(value)

    companion object {
        const val TABLE_NAME = "EventInstanceDoubleFields"
    }
}