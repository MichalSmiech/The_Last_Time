package com.michasoft.thelasttime.model

/**
 * Created by mśmiech on 31.10.2020.
 */
class Event(
    var name: String
) {
    var id: Long? = null
    private var instances: List<EventInstance>? = null
    private var tags: List<Tag>? = null
    private var instanceSchema: EventInstanceSchema? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Event

        if (id != other.id) return false
        if (name != other.name) return false
        if (instances != other.instances) return false
        if (tags != other.tags) return false
        if (instanceSchema != other.instanceSchema) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name.hashCode() ?: 0)
        result = 31 * result + (instances?.hashCode() ?: 0)
        result = 31 * result + (tags?.hashCode() ?: 0)
        result = 31 * result + (instanceSchema?.hashCode() ?: 0)
        return result
    }


}