package com.michasoft.thelasttime.model.syncJob

import com.michasoft.thelasttime.dataSource.ILocalEventSource
import com.michasoft.thelasttime.dataSource.IRemoteEventSource
import com.michasoft.thelasttime.di.UserSessionComponent
import com.michasoft.thelasttime.util.IdGenerator
import javax.inject.Inject

/**
 * Created by mśmiech on 21.09.2023.
 */
class EventInstanceSyncJob(
    id: String,
    action: Action,
    state: State,
    serializedData: String,
) : SyncJob(
    id,
    TYPE,
    action,
    state,
    serializedData,
) {
    val eventId: String
    val instanceId: String

    init {
        serializedData.split(";").let {
            eventId = it[0]
            instanceId = it[1]
        }
    }

    @Inject
    lateinit var localSource: ILocalEventSource

    @Inject
    lateinit var remoteSource: IRemoteEventSource

    override fun copy(state: State): EventInstanceSyncJob {
        return EventInstanceSyncJob(id, action, state, serializedData)
    }

    override fun inject(userSessionComponent: UserSessionComponent) {
        userSessionComponent.inject(this)
    }

    override suspend fun insert(): State {
        val eventInstance = localSource.getEventInstance(eventId, instanceId)!!
        remoteSource.insertEventInstance(eventInstance)
        return State.Completed
    }

    override suspend fun update(): State {
        val eventInstance = localSource.getEventInstance(eventId, instanceId)!!
        remoteSource.updateEventInstance(eventInstance)
        return State.Completed
    }

    override suspend fun delete(): State {
        remoteSource.deleteEventInstance(eventId, instanceId)
        return State.Completed
    }

    object Factory : SyncJob.Factory {
        override fun create(
            id: String,
            type: String,
            action: SyncJob.Action,
            state: SyncJob.State,
            serializedData: String,
        ): EventInstanceSyncJob {
            return EventInstanceSyncJob(
                id,
                action,
                state,
                serializedData,
            )
        }

        fun create(eventId: String, eventInstanceId: String, action: Action): EventInstanceSyncJob {
            return EventInstanceSyncJob(
                IdGenerator.newId(),
                action,
                State.New,
                "$eventId;$eventInstanceId"
            )
        }
    }

    companion object {
        const val TYPE = "EventInstanceSyncJob"
    }
}