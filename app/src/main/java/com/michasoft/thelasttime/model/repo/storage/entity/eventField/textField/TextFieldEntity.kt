package com.michasoft.thelasttime.model.repo.storage.entity.eventField.textField

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.michasoft.thelasttime.model.repo.storage.entity.eventField.EventFieldEntity

/**
 * Created by mśmiech on 01.05.2021.
 */

@Entity(tableName = TextFieldEntity.TABLE_NAME)
class TextFieldEntity(
    val text: String,
    eventId: Long,
    eventFieldId: Long
): EventFieldEntity(
    eventId,
    eventFieldId
) {
    companion object {
        const val TABLE_NAME = "TextFields"
    }
}