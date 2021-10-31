package com.michasoft.thelasttime.model.storage.entity.eventInstanceField

import androidx.room.Entity
import com.michasoft.thelasttime.model.eventInstanceField.TextField

/**
 * Created by mśmiech on 31.10.2021.
 */

@Entity(tableName = EventInstanceTextFieldEntity.TABLE_NAME)
class EventInstanceTextFieldEntity(
    val instanceId: Long,
    val fieldSchemaId: Long,
    val value: String?
) {
    fun toModel() = TextField(value)

    companion object {
        const val TABLE_NAME = "EventInstanceTextFields"
    }
}