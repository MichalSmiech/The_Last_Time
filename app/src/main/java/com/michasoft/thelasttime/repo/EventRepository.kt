package com.michasoft.thelasttime.repo

import com.michasoft.thelasttime.dataSource.ILocalEventSource
import com.michasoft.thelasttime.dataSource.RoomReminderSource
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.Label
import com.michasoft.thelasttime.model.SyncJobQueue
import com.michasoft.thelasttime.model.SyncJobQueueCoordinator
import com.michasoft.thelasttime.model.syncJob.EventInstanceSyncJob
import com.michasoft.thelasttime.model.syncJob.EventSyncJob
import com.michasoft.thelasttime.model.syncJob.SyncJob
import com.michasoft.thelasttime.util.BackupConfig
import com.michasoft.thelasttime.util.EventInstanceFactory
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 * Created by mśmiech on 01.11.2021.
 */
class EventRepository(
    private val localEventSource: ILocalEventSource,
    private val backupConfig: BackupConfig,
    private val syncJobQueue: SyncJobQueue,
    private val syncJobQueueCoordinator: SyncJobQueueCoordinator,
    private val localReminderSource: RoomReminderSource
) {
    private val _eventsChanged: MutableSharedFlow<Unit> = MutableSharedFlow()
    val eventsChanged: SharedFlow<Unit> = _eventsChanged

    suspend fun insertEvent(event: Event) {
        localEventSource.insertEvent(event)
        if (backupConfig.isAutoBackup()) {
            val syncJob = EventSyncJob.Factory.create(event.id, SyncJob.Action.Insert)
            syncJobQueue.add(syncJob)
            syncJobQueueCoordinator.triggerSync()
        }
        _eventsChanged.emit(Unit)
    }

    suspend fun getEvent(
        eventId: String,
        withLabels: Boolean = false,
    ): Event? {
        return localEventSource.getEvent(eventId)?.also { event ->
            if (withLabels) {
                event.labels = localEventSource.getEventLabels(event.id)
            }
        }
    }

    suspend fun getEvents(): ArrayList<Event> {
        return localEventSource.getAllEventsAtOnce()
    }

    suspend fun deleteEventInstance(eventId: String, instanceId: String) {
        localEventSource.deleteEventInstance(eventId, instanceId)
        if (backupConfig.isAutoBackup()) {
            val syncJob =
                EventInstanceSyncJob.Factory.create(eventId, instanceId, SyncJob.Action.Delete)
            syncJobQueue.add(syncJob)
            syncJobQueueCoordinator.triggerSync()
        }
        _eventsChanged.emit(Unit)
    }

    suspend fun getEvents(
        withLastInstanceTimestamp: Boolean = false,
        withLabels: Boolean = false,
        withReminders: Boolean = false
    ): ArrayList<Event> {
        val events = getEvents()
        events.forEach { event ->
            if (withLastInstanceTimestamp) {
                event.lastInstanceTimestamp = localEventSource.getLastInstanceTimestamp(event.id)
            }
            if (withLabels) {
                event.labels = localEventSource.getEventLabels(event.id)
            }
            if (withReminders) {
                event.reminders = localReminderSource.getEventReminders(event.id)
            }
        }
        return events
    }

    suspend fun updateEvent(event: Event) {
        //TODO check if eventInstanceSchema changed then have to update all instances
        localEventSource.updateEvent(event)
        if (backupConfig.isAutoBackup()) {
            val syncJob = EventSyncJob.Factory.create(event.id, SyncJob.Action.Update)
            syncJobQueue.add(syncJob)
            syncJobQueueCoordinator.triggerSync()
        }
        _eventsChanged.emit(Unit)
    }

    suspend fun updateEventInstance(instance: EventInstance) {
        localEventSource.updateEventInstance(instance)
        if (backupConfig.isAutoBackup()) {
            val syncJob = EventInstanceSyncJob.Factory.create(
                instance.eventId,
                instance.id,
                SyncJob.Action.Update
            )
            syncJobQueue.add(syncJob)
            syncJobQueueCoordinator.triggerSync()
        }
        _eventsChanged.emit(Unit)
    }

    suspend fun deleteEvent(eventId: String) {
        localEventSource.deleteEvent(eventId)
        if (backupConfig.isAutoBackup()) {
            val syncJob = EventSyncJob.Factory.create(eventId, SyncJob.Action.Delete)
            syncJobQueue.add(syncJob)
            syncJobQueueCoordinator.triggerSync()
        }
        _eventsChanged.emit(Unit)
    }

    suspend fun insertEventInstance(instance: EventInstance) {
        localEventSource.insertEventInstance(instance)
        if (backupConfig.isAutoBackup()) {
            val syncJob = EventInstanceSyncJob.Factory.create(
                instance.eventId,
                instance.id,
                SyncJob.Action.Insert
            )
            syncJobQueue.add(syncJob)
            syncJobQueueCoordinator.triggerSync()
        }
        _eventsChanged.emit(Unit)
    }

    suspend fun getEventInstance(eventId: String, instanceId: String): EventInstance? {
        return localEventSource.getEventInstance(eventId, instanceId)
    }

    suspend fun createEventInstance(eventId: String): EventInstance {
        val event = getEvent(eventId)!!
        return EventInstanceFactory.createEmptyEventInstance(event)
    }

    suspend fun getEventInstances(eventId: String): List<EventInstance> {
        return localEventSource.getAllEventInstancesAtOnce(eventId)
    }

    suspend fun getLabels(): List<Label> {
        return localEventSource.getLabels()
    }

    suspend fun getEventLabels(eventId: String): List<Label> {
        return localEventSource.getEventLabels(eventId)
    }

    suspend fun insertLabel(label: Label) {
        localEventSource.insertLabel(label)
        //TODO sync
    }

    suspend fun insertEventLabel(eventId: String, labelId: String) {
        localEventSource.insertEventLabel(eventId, labelId)
        //TODO sync
    }

    suspend fun deleteEventLabel(eventId: String, labelId: String) {
        localEventSource.deleteEventLabel(eventId, labelId)
        //TODO sync
    }
}