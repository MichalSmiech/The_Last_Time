package com.michasoft.thelasttime.model.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.michasoft.thelasttime.model.storage.dao.EventDao
import com.michasoft.thelasttime.model.storage.entity.EventInstanceEntity
import com.michasoft.thelasttime.model.storage.entity.EventEntity
import com.michasoft.thelasttime.model.storage.entity.EventInstanceFieldSchemaEntity
import com.michasoft.thelasttime.model.storage.entity.eventInstanceField.EventInstanceDoubleFieldEntity
import com.michasoft.thelasttime.model.storage.entity.eventInstanceField.EventInstanceIntFieldEntity
import com.michasoft.thelasttime.model.storage.entity.eventInstanceField.EventInstanceTextFieldEntity
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
    EventInstanceTextFieldEntity::class
], version = 1, exportSchema = false)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val eventDao: EventDao

    companion object {
        fun build(context: Context) : AppDatabase {
            val databaseName = "AppDatabase.db"
            return Room.databaseBuilder(context, AppDatabase::class.java, databaseName)
                .build()
        }
    }
}