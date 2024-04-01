package com.michasoft.thelasttime.model

import com.michasoft.thelasttime.model.syncJob.EventInstanceSyncJob
import com.michasoft.thelasttime.model.syncJob.EventLabelSyncJob
import com.michasoft.thelasttime.model.syncJob.EventSyncJob
import com.michasoft.thelasttime.model.syncJob.LabelSyncJob
import com.michasoft.thelasttime.model.syncJob.ReminderSyncJob
import com.michasoft.thelasttime.model.syncJob.SyncJob

/**
 * Created by mśmiech on 21.09.2023.
 */
object SyncJobFactory {
    private val factories = mapOf<String, SyncJob.Factory>(
        EventSyncJob.TYPE to EventSyncJob.Factory,
        EventInstanceSyncJob.TYPE to EventInstanceSyncJob.Factory,
        ReminderSyncJob.TYPE to ReminderSyncJob.Factory,
        LabelSyncJob.TYPE to LabelSyncJob.Factory,
        EventLabelSyncJob.TYPE to EventLabelSyncJob.Factory,
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