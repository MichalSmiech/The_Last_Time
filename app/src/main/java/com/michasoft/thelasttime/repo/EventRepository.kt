package com.michasoft.thelasttime.repo

import com.michasoft.thelasttime.cache.EventInstancesCountCache
import com.michasoft.thelasttime.dataSource.ILocalEventSource
import com.michasoft.thelasttime.dataSource.RoomLabelSource
import com.michasoft.thelasttime.dataSource.RoomReminderSource
import com.michasoft.thelasttime.model.DateRange
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.SyncJobQueue
import com.michasoft.thelasttime.model.SyncJobQueueCoordinator
import com.michasoft.thelasttime.model.syncJob.EventInstanceSyncJob
import com.michasoft.thelasttime.model.syncJob.EventSyncJob
import com.michasoft.thelasttime.model.syncJob.SyncJob
import com.michasoft.thelasttime.util.BackupConfig
import com.michasoft.thelasttime.util.EventInstanceFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.joda.time.LocalDate
import kotlin.math.max

/**
 * Created by mśmiech on 01.11.2021.
 */
class EventRepository(
    private val localEventSource: ILocalEventSource,
    private val backupConfig: BackupConfig,
    private val syncJobQueue: SyncJobQueue,
    private val syncJobQueueCoordinator: SyncJobQueueCoordinator,
    private val localReminderSource: RoomReminderSource,
    private val localLabelSource: RoomLabelSource,
    private val eventInstancesCountCache: EventInstancesCountCache
) {
    /**
     * eventId
     */
    private val _eventsChanged: MutableSharedFlow<String> = MutableSharedFlow()

    /**
     * eventId
     */
    val eventsChanged: SharedFlow<String> = _eventsChanged

    init {
        CoroutineScope(Dispatchers.IO).launch {
            eventsChanged.collect { eventId ->
                eventInstancesCountCache.invalid(eventId)
            }
        }
    }

    suspend fun insertEvent(event: Event) {
        localEventSource.insertEvent(event)
        if (backupConfig.isAutoBackup()) {
            val syncJob = EventSyncJob.Factory.create(event.id, SyncJob.Action.Insert)
            syncJobQueue.add(syncJob)
            syncJobQueueCoordinator.triggerSync()
        }
        _eventsChanged.emit(event.id)
    }

    suspend fun getEvent(eventId: String): Event? {
        return localEventSource.getEvent(eventId)
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
        _eventsChanged.emit(eventId)
    }

    suspend fun getEvents(
        withLastInstanceTimestamp: Boolean = false,
        withLabels: Boolean = false,
        withReminders: Boolean = false
    ): List<Event> {
        val events = getEvents()
        events.forEach { event ->
            if (withLastInstanceTimestamp) {
                event.lastInstanceTimestamp = localEventSource.getLastInstanceTimestamp(event.id)
            }
            if (withLabels) {
                event.labels = localLabelSource.getEventLabels(event.id)
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
        _eventsChanged.emit(event.id)
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
        _eventsChanged.emit(instance.eventId)
    }

    suspend fun deleteEvent(eventId: String) {
        localEventSource.deleteEvent(eventId)
        if (backupConfig.isAutoBackup()) {
            val syncJob = EventSyncJob.Factory.create(eventId, SyncJob.Action.Delete)
            syncJobQueue.add(syncJob)
            syncJobQueueCoordinator.triggerSync()
        }
        _eventsChanged.emit(eventId)
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
        _eventsChanged.emit(instance.eventId)
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

    suspend fun getEventInstancesCount(eventId: String, date: LocalDate): Int {
        return eventInstancesCountCache.getEventInstancesCount(eventId, date)
    }

    suspend fun getMaxEventInstancesCountInDateRange(eventId: String, dateRange: DateRange): Int {
        var maxValue = 0
        dateRange.getDates().forEach { date ->
            val value = eventInstancesCountCache.getEventInstancesCount(eventId, date)
            maxValue = max(maxValue, value)
        }
        return maxValue
    }
}