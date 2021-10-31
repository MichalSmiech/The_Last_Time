package com.michasoft.thelasttime.model.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.michasoft.thelasttime.model.Event

/**
 * Created by mśmiech on 11.11.2020.
 */
@Entity(tableName = EventEntity.TABLE_NAME)
class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val displayName: String
) {
    fun toModel() = Event(id, displayName)

    companion object {
        const val TABLE_NAME = "Events"
    }
}