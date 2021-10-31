package com.michasoft.thelasttime.model

/**
 * Created by mśmiech on 31.10.2021.
 */
abstract class EventInstanceField(
    val fieldSchemaId: Long,
    val type: Type
) {
    enum class Type {
        TextField,
        IntField,
        DoubleField
    }
}