package com.michasoft.thelasttime.model.storage.entity.eventInstanceField

import androidx.room.Entity
import com.michasoft.thelasttime.model.eventInstanceField.IntField

/**
 * Created by mśmiech on 31.10.2021.
 */
@Entity(tableName = EventInstanceIntFieldEntity.TABLE_NAME)
class EventInstanceIntFieldEntity(
    val instanceId: Long,
    val fieldSchemaId: Long,
    val value: Int?
) {
    fun toModel() = IntField(value)

    companion object {
        const val TABLE_NAME = "EventInstanceIntFields"
    }
}