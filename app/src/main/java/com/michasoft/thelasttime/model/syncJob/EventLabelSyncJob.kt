package com.michasoft.thelasttime.model.syncJob

import com.michasoft.thelasttime.dataSource.FirestoreLabelSource
import com.michasoft.thelasttime.di.UserSessionComponent
import com.michasoft.thelasttime.util.IdGenerator
import javax.inject.Inject

class EventLabelSyncJob(
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
    private val eventId: String
    private val labelId: String

    init {
        val split = serializedData.split(";")
        eventId = split.first()
        labelId = split.last()
    }

    @Inject
    lateinit var remoteLabelSource: FirestoreLabelSource

    override fun copy(state: State): LabelSyncJob {
        return LabelSyncJob(id, action, state, serializedData)
    }

    override fun inject(userSessionComponent: UserSessionComponent) {
        userSessionComponent.inject(this)
    }

    override suspend fun insert(): State {
        remoteLabelSource.insertEventLabel(eventId, labelId)
        return State.Completed
    }

    override suspend fun update(): State {
        return State.Error
    }

    override suspend fun delete(): State {
        remoteLabelSource.deleteEventLabel(eventId, labelId)
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

        fun create(eventId: String, labelId: String, action: Action): EventLabelSyncJob {
            return EventLabelSyncJob(IdGenerator.newId(), action, State.New, "${eventId};${labelId}")
        }
    }

    companion object {
        const val TYPE = "EventLabelSyncJob"
    }
}