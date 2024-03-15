package com.michasoft.thelasttime.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.michasoft.thelasttime.storage.entity.EventLabelEntity
import com.michasoft.thelasttime.storage.entity.LabelEntity

@Dao
interface LabelDao {
    @Insert
    suspend fun insertLabel(labelEntity: LabelEntity)

    @Query("DELETE FROM ${LabelEntity.TABLE_NAME} WHERE id=:labelId")
    suspend fun deleteLabel(labelId: String)

    @Query("DELETE FROM ${LabelEntity.TABLE_NAME}")
    suspend fun deleteAllLabel()

    @Query("UPDATE ${LabelEntity.TABLE_NAME} SET name = :name WHERE id = :labelId")
    suspend fun updateLabelName(labelId: String, name: String)

    @Insert
    suspend fun insertEventLabel(eventLabelEntity: EventLabelEntity)

    @Query("DELETE FROM ${EventLabelEntity.TABLE_NAME} WHERE eventId=:eventId AND labelId=:labelId")
    suspend fun deleteEventLabel(eventId: String, labelId: String)

    @Query("DELETE FROM ${EventLabelEntity.TABLE_NAME} WHERE labelId=:labelId")
    suspend fun deleteAllEventLabels(labelId: String)

    @Query("DELETE FROM ${EventLabelEntity.TABLE_NAME}")
    suspend fun deleteAllEventLabels()

    @Query("DELETE FROM ${EventLabelEntity.TABLE_NAME} WHERE eventId=:eventId")
    suspend fun deleteEventAllLabels(eventId: String)

    @Query("SELECT label.* FROM ${EventLabelEntity.TABLE_NAME} as eventLabel JOIN ${LabelEntity.TABLE_NAME} AS label ON eventLabel.labelId = label.id WHERE eventLabel.eventId = :eventId")
    suspend fun getEventLabels(eventId: String): List<LabelEntity>

    @Query("SELECT * FROM  ${LabelEntity.TABLE_NAME}")
    suspend fun getLabels(): List<LabelEntity>
}