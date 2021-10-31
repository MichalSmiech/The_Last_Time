package com.michasoft.thelasttime.model.eventInstanceFieldSchema

import com.michasoft.thelasttime.model.EventInstanceFieldSchema

/**
 * Created by mśmiech on 31.10.2021.
 */
class TextFieldSchema(
    id: String,
    order: Int,
    displayTitle: String
) : EventInstanceFieldSchema(
    id,
    order,
    "Text",
    displayTitle
) {
}