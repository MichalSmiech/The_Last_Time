package com.michasoft.thelasttime.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.michasoft.thelasttime.model.User
import com.michasoft.thelasttime.storage.dao.EventDao
import com.michasoft.thelasttime.storage.dao.SyncJobDao
import com.michasoft.thelasttime.storage.entity.EventEntity
import com.michasoft.thelasttime.storage.entity.EventInstanceEntity
import com.michasoft.thelasttime.storage.entity.EventInstanceFieldSchemaEntity
import com.michasoft.thelasttime.storage.entity.EventLabelEntity
import com.michasoft.thelasttime.storage.entity.LabelEntity
import com.michasoft.thelasttime.storage.entity.SyncJobEntity
import com.michasoft.thelasttime.storage.entity.eventInstanceField.EventInstanceDoubleFieldEntity
import com.michasoft.thelasttime.storage.entity.eventInstanceField.EventInstanceIntFieldEntity
import com.michasoft.thelasttime.storage.entity.eventInstanceField.EventInstanceTextFieldEntity
import com.michasoft.thelasttime.util.RoomConverters

/**
 * Created by mśmiech on 11.11.2020.
 */
@Database(entities = [
    EventEntity::class,
    EventInstanceEntity::class,
    EventInstanceFieldSchemaEntity::class,
    EventInstanceDoubleFieldEntity::class,
    EventInstanceIntFieldEntity::class,
    EventInstanceTextFieldEntity::class,
    SyncJobEntity::class,
    LabelEntity::class,
    EventLabelEntity::class,
], version = 1, exportSchema = false)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val eventDao: EventDao
    abstract val syncJobDao: SyncJobDao

    companion object {
        fun build(context: Context, user: User) : AppDatabase {
            val databaseName = "AppDatabase_${user.id}.db"
            return Room.databaseBuilder(context, AppDatabase::class.java, databaseName)
                .build()
        }
    }
}