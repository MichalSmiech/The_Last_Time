package com.michasoft.thelasttime.model.repo.storage.entity.eventField

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by mśmiech on 01.05.2021.
 */
@Entity
abstract class EventFieldEntity(
    val eventId: Long,
    @PrimaryKey
    val eventFieldId: Long
)