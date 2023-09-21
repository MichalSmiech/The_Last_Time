package com.michasoft.thelasttime.model.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.michasoft.thelasttime.model.syncJob.SyncJob

/**
 * Created by mśmiech on 21.09.2023.
 */
@Entity(tableName = SyncJobEntity.TABLE_NAME)
@TypeConverters(SyncJobEntity::class)
class SyncJobEntity(
    @PrimaryKey
    val id: String,
    val type: String,
    val action: SyncJob.Action,
    val state: SyncJob.State,
    val serializedData: String,
) {
    constructor(syncJob: SyncJob) : this(
        syncJob.id,
        syncJob.type,
        syncJob.action,
        syncJob.state,
        syncJob.serializedData
    )


    companion object {
        const val TABLE_NAME = "SyncJobs"

        @TypeConverter
        @JvmStatic
        fun fromSyncJobAction(value: SyncJob.Action): String {
            return value.toString()
        }

        @TypeConverter
        @JvmStatic
        fun toSyncJobAction(value: String): SyncJob.Action {
            return SyncJob.Action.valueOf(value)
        }

        @TypeConverter
        @JvmStatic
        fun from(value: SyncJob.State): String {
            return value.toString()
        }

        @TypeConverter
        @JvmStatic
        fun toSyncJobState(value: String): SyncJob.State {
            return SyncJob.State.valueOf(value)
        }
    }
}