package com.michasoft.thelasttime.model.repo.storage.entity.eventField

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by mśmiech on 01.05.2021.

 */

@Entity(tableName = EventFieldSchemeEntity.TABLE_NAME)
class EventFieldSchemeEntity(
    val eventTypeId: Long,
    val order: Int,
    val fieldType: String, // or Enum
    @PrimaryKey(autoGenerate = true)
    var eventFieldId: Long = 0L
) {
    companion object {
        const val TABLE_NAME = "EventFieldSchemes"
    }
}