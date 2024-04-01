package com.michasoft.thelasttime.dataSource

import androidx.room.withTransaction
import com.michasoft.thelasttime.di.UserSessionScope
import com.michasoft.thelasttime.model.Label
import com.michasoft.thelasttime.storage.AppDatabase
import com.michasoft.thelasttime.storage.dao.LabelDao
import com.michasoft.thelasttime.storage.entity.EventLabelEntity
import com.michasoft.thelasttime.storage.entity.LabelEntity
import javax.inject.Inject

@UserSessionScope
class RoomLabelSource @Inject constructor(
    private val appDatabase: AppDatabase,
    private val labelDao: LabelDao
) {
    suspend fun insertLabel(label: Label) {
        labelDao.insertLabel(LabelEntity(label))
    }

    suspend fun updateLabelName(labelId: String, name: String) {
        labelDao.updateLabelName(labelId, name)
    }

    suspend fun deleteLabel(labelId: String) {
        appDatabase.withTransaction {
            labelDao.deleteAllEventLabels(labelId)
            labelDao.deleteLabel(labelId)
        }
    }

    suspend fun insertEventLabel(eventId: String, labelId: String) {
        labelDao.insertEventLabel(EventLabelEntity(eventId, labelId))
    }

    suspend fun deleteEventLabel(eventId: String, labelId: String) {
        labelDao.deleteEventLabel(eventId, labelId)
    }

    suspend fun getEventLabels(eventId: String): List<Label> {
        return labelDao.getEventLabels(eventId).map { it.toModel() }
    }

    suspend fun getLabels(): List<Label> {
        return labelDao.getLabels().map { it.toModel() }
    }

    suspend fun getLabel(labelId: String): Label? {
        return labelDao.getLabel(labelId)?.toModel()
    }
}