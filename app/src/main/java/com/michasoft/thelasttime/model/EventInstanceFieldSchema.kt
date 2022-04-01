package com.michasoft.thelasttime.model

/**
 * Created by mśmiech on 31.10.2021.
 */
class EventInstanceFieldSchema(
    var id: String,
    val order: Int,
    val type: EventInstanceField.Type,
    val displayTitle: String
)