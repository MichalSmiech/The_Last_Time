package com.michasoft.thelasttime.model.syncJob

import com.michasoft.thelasttime.dataSource.FirestoreLabelSource
import com.michasoft.thelasttime.dataSource.RoomLabelSource
import com.michasoft.thelasttime.di.UserSessionComponent
import com.michasoft.thelasttime.util.IdGenerator
import javax.inject.Inject

class LabelSyncJob(
    id: String,
    action: SyncJob.Action,
    state: SyncJob.State,
    serializedData: String,
) : SyncJob(
    id,
    TYPE,
    action,
    state,
    serializedData,
) {
    private val labelId = serializedData

    @Inject
    lateinit var localLabelSource: RoomLabelSource

    @Inject
    lateinit var remoteLabelSource: FirestoreLabelSource

    override fun copy(state: State): LabelSyncJob {
        return LabelSyncJob(id, action, state, serializedData)
    }

    override fun inject(userSessionComponent: UserSessionComponent) {
        userSessionComponent.inject(this)
    }

    override suspend fun insert(): State {
        val label = localLabelSource.getLabel(labelId)!!
        remoteLabelSource.insertLabel(label)
        return State.Completed
    }

    override suspend fun update(): State {
        val label = localLabelSource.getLabel(labelId)!!
        remoteLabelSource.updateLabel(label)
        return State.Completed
    }

    override suspend fun delete(): State {
        remoteLabelSource.deleteLabel(labelId)
        return State.Completed
    }

    object Factory : SyncJob.Factory {
        override fun create(
            id: String,
            type: String,
            action: SyncJob.Action,
            state: SyncJob.State,
            serializedData: String,
        ): LabelSyncJob {
            return LabelSyncJob(
                id,
                action,
                state,
                serializedData,
            )
        }

        fun create(labelId: String, action: Action): LabelSyncJob {
            return LabelSyncJob(IdGenerator.newId(), action, State.New, labelId)
        }
    }

    companion object {
        const val TYPE = "LabelSyncJob"
    }
}