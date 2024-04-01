package com.michasoft.thelasttime.repo

import com.michasoft.thelasttime.dataSource.RoomLabelSource
import com.michasoft.thelasttime.di.UserSessionScope
import com.michasoft.thelasttime.model.Label
import com.michasoft.thelasttime.model.SyncJobQueue
import com.michasoft.thelasttime.model.SyncJobQueueCoordinator
import com.michasoft.thelasttime.model.syncJob.EventLabelSyncJob
import com.michasoft.thelasttime.model.syncJob.LabelSyncJob
import com.michasoft.thelasttime.model.syncJob.SyncJob
import com.michasoft.thelasttime.util.BackupConfig
import com.michasoft.thelasttime.util.notify
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Named

@UserSessionScope
class LabelRepository @Inject constructor(
    private val localLabelSource: RoomLabelSource,
    private val backupConfig: BackupConfig,
    private val syncJobQueue: SyncJobQueue,
    private val syncJobQueueCoordinator: SyncJobQueueCoordinator,
    @Named("labelsChanged") val labelsChanged: MutableSharedFlow<Unit>,
) {

    suspend fun getLabels(): List<Label> {
        return localLabelSource.getLabels()
    }

    suspend fun getEventLabels(eventId: String): List<Label> {
        return localLabelSource.getEventLabels(eventId)
    }

    suspend fun insertLabel(label: Label) {
        localLabelSource.insertLabel(label)
        if (backupConfig.isAutoBackup()) {
            val syncJob = LabelSyncJob.Factory.create(label.id, SyncJob.Action.Insert)
            syncJobQueue.add(syncJob)
            syncJobQueueCoordinator.triggerSync()
        }
        labelsChanged.notify()
    }

    suspend fun updateLabelName(labelId: String, name: String) {
        localLabelSource.updateLabelName(labelId, name)
        if (backupConfig.isAutoBackup()) {
            val syncJob = LabelSyncJob.Factory.create(labelId, SyncJob.Action.Update)
            syncJobQueue.add(syncJob)
            syncJobQueueCoordinator.triggerSync()
        }
        labelsChanged.notify()
    }

    suspend fun deleteLabel(labelId: String) {
        localLabelSource.deleteLabel(labelId)
        if (backupConfig.isAutoBackup()) {
            val syncJob = LabelSyncJob.Factory.create(labelId, SyncJob.Action.Delete)
            syncJobQueue.add(syncJob)
            syncJobQueueCoordinator.triggerSync()
        }
        labelsChanged.notify()
    }

    suspend fun insertEventLabel(eventId: String, labelId: String) {
        localLabelSource.insertEventLabel(eventId, labelId)
        if (backupConfig.isAutoBackup()) {
            val syncJob = EventLabelSyncJob.Factory.create(eventId, labelId, SyncJob.Action.Insert)
            syncJobQueue.add(syncJob)
            syncJobQueueCoordinator.triggerSync()
        }
        labelsChanged.notify()
    }

    suspend fun deleteEventLabel(eventId: String, labelId: String) {
        localLabelSource.deleteEventLabel(eventId, labelId)
        if (backupConfig.isAutoBackup()) {
            val syncJob = EventLabelSyncJob.Factory.create(eventId, labelId, SyncJob.Action.Delete)
            syncJobQueue.add(syncJob)
            syncJobQueueCoordinator.triggerSync()
        }
        labelsChanged.notify()
    }

    suspend fun deleteEventAllLabels(eventId: String) {
        getEventLabels(eventId).forEach {
            deleteEventLabel(eventId, it.id)
        }
    }
}