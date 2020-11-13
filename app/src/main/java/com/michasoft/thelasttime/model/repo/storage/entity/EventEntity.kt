package com.michasoft.thelasttime.model.repo.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.michasoft.thelasttime.model.Event

/**
 * Created by mśmiech on 11.11.2020.
 */

@Entity(tableName = EventEntity.TABLE_NAME)
class EventEntity(
    val name: String,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
) {
    constructor(event: Event) : this(
        event.name
    ) {
        event.id?.let {
            this.id = it
        }
    }

    fun toModel() = Event(
        name
    ).apply {
        id = this@EventEntity.id
    }

    companion object {
        const val TABLE_NAME = "Events"
    }
}