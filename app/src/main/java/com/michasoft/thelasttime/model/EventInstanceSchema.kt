package com.michasoft.thelasttime.model

/**
 * Created by mśmiech on 31.10.2021.
 */
class EventInstanceSchema() {
    val fieldSchemas: ArrayList<EventInstanceFieldSchema> = ArrayList()

    constructor(fieldSchemas: List<EventInstanceFieldSchema>): this() {
        this.fieldSchemas.addAll(fieldSchemas)
    }
}