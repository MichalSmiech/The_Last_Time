package com.michasoft.thelasttime.model

import com.google.firebase.Timestamp
import org.joda.time.DateTime
import kotlin.collections.ArrayList

/**
 * Created by mśmiech on 08.07.2021.
 */
data class EventInstance(
    var id: String,
    val eventId: String,
    val timestamp: DateTime,
    val fields: ArrayList<EventInstanceField>
) {
    constructor(id: String, eventId: String, map: Map<String, Any?>, instanceSchema: EventInstanceSchema)
            : this(
        id,
        eventId,
        DateTime((map["timestamp"] as Timestamp).toDate()),
        ArrayList()
    ) {
        instanceSchema.fieldSchemas.forEach { fieldSchema ->
            val field = fieldSchema.type.eventInstanceFieldFactory.create(fieldSchema, map)
            fields.add(field)
        }
    }

    fun toMap(): Map<String, Any?> {
        val map = hashMapOf<String, Any?>()
        map["timestamp"] = Timestamp(timestamp.toDate())
        fields.forEach {
            map.putAll(it.toMap())
        }
        return map
    }
}