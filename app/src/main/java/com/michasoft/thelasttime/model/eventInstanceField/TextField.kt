package com.michasoft.thelasttime.model.eventInstanceField

import com.michasoft.thelasttime.model.EventInstanceField

/**
 * Created by mśmiech on 31.10.2021.
 */
class TextField(
    fieldSchemaId: Long,
    val value: String?
) : EventInstanceField(fieldSchemaId, Type.TextField)