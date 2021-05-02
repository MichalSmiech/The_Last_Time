package com.michasoft.thelasttime.model.repo.storage.entity.eventField.textField

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by mśmiech on 01.05.2021.
 */

@Entity(tableName = TextFieldSchemeEntity.TABLE_NAME)
class TextFieldSchemeEntity(
    val title: String,
    @PrimaryKey
    val eventFieldId: Long
) {
    companion object {
        const val TABLE_NAME = "EventTextFieldSchemes"
    }
}