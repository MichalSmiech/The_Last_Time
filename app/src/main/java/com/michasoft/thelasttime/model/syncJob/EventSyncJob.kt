package com.michasoft.thelasttime.model.syncJob

import com.michasoft.thelasttime.di.UserSessionComponent
import com.michasoft.thelasttime.model.dataSource.ILocalEventSource
import com.michasoft.thelasttime.model.dataSource.IRemoteEventSource
import com.michasoft.thelasttime.util.IdGenerator
import javax.inject.Inject

/**
 * Created by mśmiech on 20.09.2023.
 */
class EventSyncJob(
    id: String,
    action: Action,
    state: State,
    serializedData: String,
) : SyncJob(
    id,
    "EventSyncJob",
    action,
    state,
    serializedData,
    Factory,
) {
    val eventId = serializedData

    @Inject
    lateinit var roomEventSource: ILocalEventSource

    @Inject
    lateinit var firestoreEventSource: IRemoteEventSource

    override fun copy(state: State): EventSyncJob {
        return EventSyncJob(id, action, state, serializedData)
    }

    override fun inject(userSessionComponent: UserSessionComponent) {
        userSessionComponent.inject(this)
    }

    override suspend fun insert(): State {
        val event = roomEventSource.getEvent(eventId)!!
        firestoreEventSource.insertEvent(event)
        return State.Completed
    }

    override suspend fun update(): State {
        val event = roomEventSource.getEvent(eventId)!!
        firestoreEventSource.updateEvent(event)
        return State.Completed
    }

    override suspend fun delete(): State {
        firestoreEventSource.deleteEvent(eventId)
        return State.Completed
    }

    object Factory : SyncJob.Factory {
        override fun create(): SyncJob {
            TODO("Not yet implemented")
        }

        fun create(eventId: String, action: Action): EventSyncJob {
            return EventSyncJob(IdGenerator.newId(), action, State.New, eventId)
        }
    }
}