package com.michasoft.thelasttime.repo

import com.michasoft.thelasttime.dataSource.FirestoreReminderSource
import com.michasoft.thelasttime.dataSource.ILocalEventSource
import com.michasoft.thelasttime.dataSource.IRemoteEventSource
import com.michasoft.thelasttime.dataSource.RoomLabelSource
import com.michasoft.thelasttime.dataSource.RoomReminderSource
import com.michasoft.thelasttime.reminder.CancelReminderUseCase
import com.michasoft.thelasttime.reminder.ScheduleReminderUseCase
import javax.inject.Inject

/**
 * Created by mśmiech on 08.11.2021.
 */
class BackupRepository @Inject constructor(
    private val localEventSource: ILocalEventSource,
    private val remoteEventSource: IRemoteEventSource,
    private val localReminderSource: RoomReminderSource,
    private val remoteReminderSource: FirestoreReminderSource,
    private val scheduleReminderUseCase: ScheduleReminderUseCase,
    private val cancelReminderUseCase: CancelReminderUseCase,
    private val localLabelSource: RoomLabelSource
) {
    suspend fun clearLocalDatabase() {
        localEventSource.clear()
        localReminderSource.clear()
        localLabelSource.clear()
    }

    suspend fun clearBackup() {
        remoteEventSource.clear()
        remoteReminderSource.clear()
        //TODO labels
    }

    suspend fun restoreBackup() {
        restoreEvents()
        restoreReminders()
    }

    private suspend fun restoreEvents() {
        remoteEventSource.getAllEvents()
            .collect { event ->
                localEventSource.insertEvent(event)
                remoteEventSource.getAllEventInstances(event.id, event.eventInstanceSchema)
                    .collect { instance ->
                        localEventSource.insertEventInstance(instance)
                    }
            }
    }

    private suspend fun restoreReminders() {
        remoteReminderSource.getAllReminders()
            .collect { reminder ->
                localReminderSource.insertReminder(reminder)
                cancelReminderUseCase.execute(reminder)
                scheduleReminderUseCase.execute(reminder)
            }
    }
}