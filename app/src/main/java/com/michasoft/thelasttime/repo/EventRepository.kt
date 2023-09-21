package com.michasoft.thelasttime.repo

import com.michasoft.thelasttime.dataSource.ILocalEventSource
import com.michasoft.thelasttime.dataSource.IRemoteEventSource
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.SyncJobQueue
import com.michasoft.thelasttime.model.SyncJobQueueCoordinator
import com.michasoft.thelasttime.model.syncJob.EventSyncJob
import com.michasoft.thelasttime.model.syncJob.SyncJob
import com.michasoft.thelasttime.util.BackupConfig
import com.michasoft.thelasttime.util.EventInstanceFactory

/**
 * Created by mśmiech on 01.11.2021.
 */
class EventRepository(
    private val localSource: ILocalEventSource,
    private val remoteSource: IRemoteEventSource,
    private val backupConfig: BackupConfig,
    private val syncJobQueue: SyncJobQueue,
    private val syncJobQueueCoordinator: SyncJobQueueCoordinator
) {
    suspend fun insert(event: Event) {
        localSource.insertEvent(event)
        if (backupConfig.isAutoBackup()) {
            val syncJob = EventSyncJob.Factory.create(event.id, SyncJob.Action.Insert)
            syncJobQueue.add(syncJob)
            syncJobQueueCoordinator.triggerSync()
        }
    }

    suspend fun getEvent(eventId: String): Event? {
        return localSource.getEvent(eventId)
    }

    suspend fun getEvents(): ArrayList<Event> {
        return localSource.getAllEventsAtOnce()
    }

    suspend fun deleteEventInstance(eventId: String, instanceId: String) {
        localSource.deleteEventInstance(eventId, instanceId)
        if (backupConfig.isAutoBackup()) {
            remoteSource.deleteEventInstance(eventId, instanceId)
        }
    }

    suspend fun getEventsWithLastInstanceTimestamp(): ArrayList<Event> {
        val events = getEvents()
        events.forEach { event ->
            event.lastInstanceTimestamp = localSource.getLastInstanceTimestamp(event.id)
        }
        return events
    }

    suspend fun update(event: Event) {
        //TODO check if eventInstanceSchema changed then have to update all instances
        localSource.updateEvent(event)
        if (backupConfig.isAutoBackup()) {
            val syncJob = EventSyncJob.Factory.create(event.id, SyncJob.Action.Update)
            syncJobQueue.add(syncJob)
            syncJobQueueCoordinator.triggerSync()
        }
    }

    suspend fun update(instance: EventInstance) {
        localSource.updateEventInstance(instance)
        if (backupConfig.isAutoBackup()) {
            remoteSource.updateEventInstance(instance)
        }
    }

    suspend fun deleteEvent(eventId: String) {
        localSource.deleteEvent(eventId)
        if (backupConfig.isAutoBackup()) {
            val syncJob = EventSyncJob.Factory.create(eventId, SyncJob.Action.Delete)
            syncJobQueue.add(syncJob)
            syncJobQueueCoordinator.triggerSync()
        }
    }

    suspend fun insert(instance: EventInstance) {
        localSource.insertEventInstance(instance)
        if (backupConfig.isAutoBackup()) {
            remoteSource.insertEventInstance(instance)
        }
    }

    suspend fun getEventInstance(eventId: String, instanceId: String): EventInstance? {
        return localSource.getEventInstance(eventId, instanceId)
    }

    suspend fun createEventInstance(eventId: String): EventInstance {
        val event = getEvent(eventId)!!
        return EventInstanceFactory.createEmptyEventInstance(event)
    }

    suspend fun getEventInstances(eventId: String): List<EventInstance> {
        return localSource.getAllEventInstancesAtOnce(eventId)
    }
}