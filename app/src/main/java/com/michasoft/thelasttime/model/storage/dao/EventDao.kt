package com.michasoft.thelasttime.model.storage.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.michasoft.thelasttime.model.storage.entity.EventEntity
import com.michasoft.thelasttime.model.storage.entity.EventInstanceEntity
import com.michasoft.thelasttime.model.storage.entity.EventInstanceFieldSchemaEntity
import com.michasoft.thelasttime.model.storage.entity.eventInstanceField.EventInstanceDoubleFieldEntity
import com.michasoft.thelasttime.model.storage.entity.eventInstanceField.EventInstanceIntFieldEntity
import com.michasoft.thelasttime.model.storage.entity.eventInstanceField.EventInstanceTextFieldEntity

/**
 * Created by mśmiech on 11.11.2020.
 */

@Dao
interface EventDao {
    @Insert
    suspend fun insertEvent(event: EventEntity): Long

    @Update
    suspend fun updateEvent(event: EventEntity)

    @Query("SELECT * FROM ${EventEntity.TABLE_NAME} WHERE id=:id")
    suspend fun getEvent(id: Long): EventEntity?

    @Query("SELECT * FROM ${EventInstanceEntity.TABLE_NAME} WHERE id=:id")
    suspend fun getEventInstance(id: Long): EventInstanceEntity?

    @Insert
    suspend fun insertEventInstance(instance: EventInstanceEntity): Long

    @Query("DELETE FROM ${EventInstanceEntity.TABLE_NAME} WHERE id=:id")
    suspend fun deleteEventInstance(id: Long)

    @Query("SELECT * FROM ${EventInstanceFieldSchemaEntity.TABLE_NAME} WHERE eventId=:eventId")
    suspend fun getEventInstanceFieldSchemas(eventId: Long): List<EventInstanceFieldSchemaEntity>

    @Insert
    suspend fun insertEventInstanceFieldSchema(schema: EventInstanceFieldSchemaEntity): Long

    @Query("SELECT * FROM ${EventInstanceTextFieldEntity.TABLE_NAME} WHERE instanceId=:instanceId AND fieldSchemaId=:fieldSchemaId")
    suspend fun getEventInstanceTextField(instanceId: Long, fieldSchemaId: Long): EventInstanceTextFieldEntity

    // TODO https://stackoverflow.com/a/66802331
    @Insert
    suspend fun insertEventInstanceTextField(field: EventInstanceTextFieldEntity)

    @Query("DELETE FROM ${EventInstanceTextFieldEntity.TABLE_NAME} WHERE instanceId=:instanceId")
    suspend fun deleteEventInstanceTextFieldsWithInstanceId(instanceId: Long)

    @Query("SELECT * FROM ${EventInstanceIntFieldEntity.TABLE_NAME} WHERE instanceId=:instanceId AND fieldSchemaId=:fieldSchemaId")
    suspend fun getEventInstanceIntField(instanceId: Long, fieldSchemaId: Long): EventInstanceIntFieldEntity

    @Insert
    suspend fun insertEventInstanceIntField(field: EventInstanceIntFieldEntity)

    @Query("DELETE FROM ${EventInstanceIntFieldEntity.TABLE_NAME} WHERE instanceId=:instanceId")
    suspend fun deleteEventInstanceIntFieldsWithInstanceId(instanceId: Long)

    @Query("SELECT * FROM ${EventInstanceDoubleFieldEntity.TABLE_NAME} WHERE instanceId=:instanceId AND fieldSchemaId=:fieldSchemaId")
    suspend fun getEventInstanceDoubleField(instanceId: Long, fieldSchemaId: Long): EventInstanceDoubleFieldEntity

    @Insert
    suspend fun insertEventInstanceDoubleField(field: EventInstanceDoubleFieldEntity)

    @Query("DELETE FROM ${EventInstanceDoubleFieldEntity.TABLE_NAME} WHERE instanceId=:instanceId")
    suspend fun deleteEventInstanceDoubleFieldsWithInstanceId(instanceId: Long)
}