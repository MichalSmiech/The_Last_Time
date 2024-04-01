package com.michasoft.thelasttime.repo

import com.michasoft.thelasttime.dataSource.FirestoreLabelSource
import com.michasoft.thelasttime.dataSource.RoomLabelSource
import com.michasoft.thelasttime.di.UserSessionScope
import com.michasoft.thelasttime.model.Label
import javax.inject.Inject

@UserSessionScope
class LabelRepository @Inject constructor(
    private val localLabelSource: RoomLabelSource,
    private val remoteLabelSource: FirestoreLabelSource
) {

    suspend fun getLabels(): List<Label> {
        return localLabelSource.getLabels()
    }

    suspend fun getEventLabels(eventId: String): List<Label> {
        return localLabelSource.getEventLabels(eventId)
    }

    suspend fun insertLabel(label: Label) {
        localLabelSource.insertLabel(label)
        //TODO sync
    }

    suspend fun updateLabelName(labelId: String, name: String) {
        localLabelSource.updateLabelName(labelId, name)
    }

    suspend fun insertEventLabel(eventId: String, labelId: String) {
        localLabelSource.insertEventLabel(eventId, labelId)
        //TODO sync
    }

    suspend fun deleteEventLabel(eventId: String, labelId: String) {
        localLabelSource.deleteEventLabel(eventId, labelId)
        //TODO sync
    }

    suspend fun deleteLabel(labelId: String) {
        localLabelSource.deleteLabel(labelId)
    }
}