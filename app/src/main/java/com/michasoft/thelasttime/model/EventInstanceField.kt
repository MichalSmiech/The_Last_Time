package com.michasoft.thelasttime.model

import com.michasoft.thelasttime.model.eventInstanceField.TextField

/**
 * Created by mśmiech on 31.10.2021.
 */
abstract class EventInstanceField(
    val fieldSchema: EventInstanceFieldSchema,
    val type: Type
) {
    abstract fun toMap(): Map<String, Any?>

    enum class Type(val eventInstanceFieldFactory: Factory) {
        TextField(com.michasoft.thelasttime.model.eventInstanceField.TextField.Factory()),
        IntField(com.michasoft.thelasttime.model.eventInstanceField.IntField.Factory()),
        DoubleField(com.michasoft.thelasttime.model.eventInstanceField.DoubleField.Factory())
    }

    interface Factory {
        fun create(schema: EventInstanceFieldSchema, map: Map<String, Any?>): EventInstanceField
    }

    companion object {
        fun getMapKey(fieldSchemaId: String, type: Type) =
            "$fieldSchemaId;${type.name}"
        fun getMapKey(instanceField: EventInstanceField) =
            getMapKey(instanceField.fieldSchema.id, instanceField.type)
        fun getMapKey(instanceSchema: EventInstanceFieldSchema) =
            getMapKey(instanceSchema.id, instanceSchema.type)
    }
}