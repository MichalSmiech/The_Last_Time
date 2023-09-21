package com.michasoft.thelasttime.model.syncJob

import com.michasoft.thelasttime.di.UserSessionComponent

/**
 * Created by mśmiech on 29.01.2023.
 */
abstract class SyncJob(
    val id: String,
    val type: String,
    val action: Action,
    val state: State,
    val serializedData: String,
    val factory: Factory
) {
    enum class State {
        New,
        Started,
        Error,
        Completed
    }

    enum class Action {
        Insert,
        Update,
        Delete
    }

    abstract fun copy(state: State = this.state): SyncJob

    abstract fun inject(userSessionComponent: UserSessionComponent)

    suspend fun run(): State {
        return when (action) {
            Action.Insert -> insert()
            Action.Update -> update()
            Action.Delete -> delete()
        }
    }

    protected abstract suspend fun insert(): State
    protected abstract suspend fun update(): State
    protected abstract suspend fun delete(): State

    interface Factory {
        fun create(
            id: String,
            type: String,
            action: SyncJob.Action,
            state: SyncJob.State,
            serializedData: String,
        ): SyncJob
    }
}