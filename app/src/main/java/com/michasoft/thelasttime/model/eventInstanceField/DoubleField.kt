package com.michasoft.thelasttime.model.eventInstanceField

import com.michasoft.thelasttime.model.EventInstanceField

/**
 * Created by mśmiech on 31.10.2021.
 */
class DoubleField(
    fieldSchemaId: Long,
    val value: Double?
) : EventInstanceField(fieldSchemaId, Type.DoubleField)