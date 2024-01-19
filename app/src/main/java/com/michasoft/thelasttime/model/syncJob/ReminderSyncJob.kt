package com.michasoft.thelasttime.model.syncJob

import com.michasoft.thelasttime.dataSource.FirestoreReminderSource
import com.michasoft.thelasttime.dataSource.RoomReminderSource
import com.michasoft.thelasttime.di.UserSessionComponent
import com.michasoft.thelasttime.model.reminder.Reminder
import com.michasoft.thelasttime.util.IdGenerator
import javax.inject.Inject

class ReminderSyncJob(
    id: String,
    action: SyncJob.Action,
    state: SyncJob.State,
    serializedData: String,
) : SyncJob(
    id,
    TYPE,
    action,
    state,
    serializedData,
) {
    private val reminderId: String
    private val reminderType: Reminder.Type

    init {
        val split = serializedData.split(";")
        reminderId = split.first()
        reminderType = Reminder.Type.valueOf(split.last())
    }

    @Inject
    lateinit var localReminderSource: RoomReminderSource

    @Inject
    lateinit var remoteReminderSource: FirestoreReminderSource

    override fun copy(state: State): ReminderSyncJob {
        return ReminderSyncJob(id, action, state, serializedData)
    }

    override fun inject(userSessionComponent: UserSessionComponent) {
        userSessionComponent.inject(this)
    }

    override suspend fun insert(): State {
        val reminder = localReminderSource.getReminder(reminderId)!!
        remoteReminderSource.insertReminder(reminder)
        return State.Completed
    }

    override suspend fun update(): State {
        return State.Error
    }

    override suspend fun delete(): State {
        remoteReminderSource.deleteReminder(reminderId, reminderType)
        return State.Completed
    }

    object Factory : SyncJob.Factory {
        override fun create(
            id: String,
            type: String,
            action: SyncJob.Action,
            state: SyncJob.State,
            serializedData: String,
        ): ReminderSyncJob {
            return ReminderSyncJob(
                id,
                action,
                state,
                serializedData,
            )
        }

        fun create(reminder: Reminder, action: Action): ReminderSyncJob {
            return ReminderSyncJob(IdGenerator.newId(), action, State.New, "${reminder.id};${reminder.type}")
        }
    }

    companion object {
        const val TYPE = "ReminderSyncJob"
    }
}