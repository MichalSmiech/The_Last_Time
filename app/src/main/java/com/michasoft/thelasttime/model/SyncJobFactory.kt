package com.michasoft.thelasttime.model

import com.michasoft.thelasttime.model.syncJob.EventSyncJob
import com.michasoft.thelasttime.model.syncJob.SyncJob

/**
 * Created by mśmiech on 21.09.2023.
 */
object SyncJobFactory {
    private val factories = mapOf<String, SyncJob.Factory>(
        EventSyncJob.TYPE to EventSyncJob.Factory
    )

    fun create(
        id: String,
        type: String,
        action: SyncJob.Action,
        state: SyncJob.State,
        serializedData: String,
    ): SyncJob {
        return factories[type]!!.create(
            id,
            type,
            action,
            state,
            serializedData,
        )
    }
}