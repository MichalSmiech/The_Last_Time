package com.michasoft.thelasttime.model.eventInstanceField

import com.michasoft.thelasttime.model.EventInstanceField
import com.michasoft.thelasttime.model.EventInstanceFieldSchema

/**
 * Created by mśmiech on 31.10.2021.
 */
class TextField(
    fieldSchemaId: Long,
    val value: String?
) : EventInstanceField(fieldSchemaId, Type.TextField) {

    override fun toMap(): Map<String, Any?> {
        return hashMapOf(
            getMapKey(this) to value
        )
    }

    class Factory: EventInstanceField.Factory {
        override fun create(schema: EventInstanceFieldSchema, map: Map<String, Any?>): EventInstanceField {
            val value = map[getMapKey(schema)] as String?
            return TextField(schema.id, value)
        }
    }
}