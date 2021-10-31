package com.michasoft.thelasttime.model.eventInstanceField

import com.michasoft.thelasttime.model.EventInstanceField

/**
 * Created by mśmiech on 31.10.2021.
 */
class IntField(
    fieldSchemaId: Long,
    val value: Int?
) : EventInstanceField(fieldSchemaId, Type.IntField)