package com.michasoft.thelasttime.model.eventInstanceField

import com.michasoft.thelasttime.model.EventInstanceField
import com.michasoft.thelasttime.model.EventInstanceFieldSchema

/**
 * Created by mśmiech on 31.10.2021.
 */
class TextField(
    fieldSchema: EventInstanceFieldSchema,
    val value: String?
) : EventInstanceField(fieldSchema, Type.TextField) {

    override fun toMap(): Map<String, Any?> {
        return hashMapOf(
            getMapKey(this) to value
        )
    }

    class Factory: EventInstanceField.Factory {
        override fun create(schema: EventInstanceFieldSchema, map: Map<String, Any?>): EventInstanceField {
            val value = map[getMapKey(schema)] as String?
            return TextField(schema, value)
        }
    }
}