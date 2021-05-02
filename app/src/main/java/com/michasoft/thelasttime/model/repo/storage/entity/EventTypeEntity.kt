package com.michasoft.thelasttime.model.repo.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.DateTime

/**
 * Created by mśmiech on 11.11.2020.
 */
@Entity(tableName = EventTypeEntity.TABLE_NAME)
class EventTypeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String
) {
    companion object {
        const val TABLE_NAME = "EventTypes"
    }
}