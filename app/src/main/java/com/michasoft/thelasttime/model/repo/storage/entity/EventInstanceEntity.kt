package com.michasoft.thelasttime.model.repo.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.DateTime

/**
 * Created by mśmiech on 11.11.2020.
 */
@Entity(tableName = EventInstanceEntity.TABLE_NAME)
class EventInstanceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val timestamp: DateTime
) {
    companion object {
        const val TABLE_NAME = "EventInstances"
    }
}