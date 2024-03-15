package com.michasoft.thelasttime.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.michasoft.thelasttime.storage.entity.EventEntity
import com.michasoft.thelasttime.storage.entity.EventInstanceEntity
import com.michasoft.thelasttime.storage.entity.EventInstanceFieldSchemaEntity
import com.michasoft.thelasttime.storage.entity.eventInstanceField.EventInstanceDoubleFieldEntity
import com.michasoft.thelasttime.storage.entity.eventInstanceField.EventInstanceIntFieldEntity
import com.michasoft.thelasttime.storage.entity.eventInstanceField.EventInstanceTextFieldEntity
import org.joda.time.DateTime

/**
 * Created by mśmiech on 11.11.2020.
 */

@Dao
interface EventDao {
    @Insert
    suspend fun insertEvent(event: EventEntity)

    @Query("SELECT * FROM ${EventEntity.TABLE_NAME} WHERE id=:id")
    suspend fun getEvent(id: String): EventEntity?

    @Query("SELECT id FROM ${EventEntity.TABLE_NAME} LIMIT :limit OFFSET :offset")
    suspend fun getEventIds(limit: Long, offset: Long): List<String>

    @Query("SELECT id FROM ${EventEntity.TABLE_NAME} ORDER BY createTimestamp DESC LIMIT :limit OFFSET :offset")
    suspend fun getEventIdsOrderByCreateTimestamp(limit: Long, offset: Long): List<String>

    @Query("UPDATE ${EventEntity.TABLE_NAME} SET name = :name WHERE id = :eventId")
    suspend fun updateEventName(eventId: String, name: String)

    @Query("DELETE FROM ${EventEntity.TABLE_NAME} WHERE id=:id")
    suspend fun deleteEvent(id: String)

    @Query("DELETE FROM ${EventEntity.TABLE_NAME}")
    suspend fun deleteAllEvents()

    @Query("SELECT * FROM ${EventInstanceEntity.TABLE_NAME} WHERE id=:id")
    suspend fun getEventInstance(id: String): EventInstanceEntity?

    @Query("SELECT id FROM ${EventInstanceEntity.TABLE_NAME} LIMIT :limit OFFSET :offset")
    suspend fun getEventInstanceIds(limit: Long, offset: Long): List<String>

    @Query("SELECT id FROM ${EventInstanceEntity.TABLE_NAME} WHERE eventId=:eventId ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    suspend fun getEventInstanceIdsWithEventIdOrderByTimestamp(eventId: String, limit: Long, offset: Long): List<String>

    @Query("SELECT id FROM ${EventInstanceEntity.TABLE_NAME} WHERE eventId=:eventId")
    suspend fun getAllEventInstanceIdsWithEventId(eventId: String): List<String>

    @Insert
    suspend fun insertEventInstance(instance: EventInstanceEntity)

    @Query("DELETE FROM ${EventInstanceEntity.TABLE_NAME} WHERE id=:id")
    suspend fun deleteEventInstance(id: String)

    @Query("DELETE FROM ${EventInstanceEntity.TABLE_NAME}")
    suspend fun deleteAllEventInstances()

    @Query("SELECT * FROM ${EventInstanceFieldSchemaEntity.TABLE_NAME} WHERE eventId=:eventId")
    suspend fun getEventInstanceFieldSchemas(eventId: String): List<EventInstanceFieldSchemaEntity>

    @Insert
    suspend fun insertEventInstanceFieldSchema(schema: EventInstanceFieldSchemaEntity)

    @Query("DELETE FROM ${EventInstanceFieldSchemaEntity.TABLE_NAME} WHERE eventId=:eventId")
    suspend fun deleteEventInstanceFieldSchemasWithEventId(eventId: String)

    @Query("DELETE FROM ${EventInstanceFieldSchemaEntity.TABLE_NAME}")
    suspend fun deleteAllEventInstanceFieldSchemas()

    @Query("SELECT * FROM ${EventInstanceTextFieldEntity.TABLE_NAME} WHERE instanceId=:instanceId AND fieldSchemaId=:fieldSchemaId")
    suspend fun getEventInstanceTextField(instanceId: String, fieldSchemaId: String): EventInstanceTextFieldEntity?

    // TODO https://stackoverflow.com/a/66802331
    @Insert
    suspend fun insertEventInstanceTextField(field: EventInstanceTextFieldEntity)

    @Query("DELETE FROM ${EventInstanceTextFieldEntity.TABLE_NAME} WHERE instanceId=:instanceId")
    suspend fun deleteEventInstanceTextFieldsWithInstanceId(instanceId: String)

    @Query("DELETE FROM ${EventInstanceTextFieldEntity.TABLE_NAME}")
    suspend fun deleteAllEventInstanceTextFields()

    @Query("SELECT * FROM ${EventInstanceIntFieldEntity.TABLE_NAME} WHERE instanceId=:instanceId AND fieldSchemaId=:fieldSchemaId")
    suspend fun getEventInstanceIntField(instanceId: String, fieldSchemaId: String): EventInstanceIntFieldEntity?

    @Insert
    suspend fun insertEventInstanceIntField(field: EventInstanceIntFieldEntity)

    @Query("DELETE FROM ${EventInstanceIntFieldEntity.TABLE_NAME} WHERE instanceId=:instanceId")
    suspend fun deleteEventInstanceIntFieldsWithInstanceId(instanceId: String)

    @Query("DELETE FROM ${EventInstanceIntFieldEntity.TABLE_NAME}")
    suspend fun deleteAllEventInstanceIntFields()

    @Query("SELECT * FROM ${EventInstanceDoubleFieldEntity.TABLE_NAME} WHERE instanceId=:instanceId AND fieldSchemaId=:fieldSchemaId")
    suspend fun getEventInstanceDoubleField(instanceId: String, fieldSchemaId: String): EventInstanceDoubleFieldEntity?

    @Insert
    suspend fun insertEventInstanceDoubleField(field: EventInstanceDoubleFieldEntity)

    @Query("DELETE FROM ${EventInstanceDoubleFieldEntity.TABLE_NAME} WHERE instanceId=:instanceId")
    suspend fun deleteEventInstanceDoubleFieldsWithInstanceId(instanceId: String)

    @Query("DELETE FROM ${EventInstanceDoubleFieldEntity.TABLE_NAME}")
    suspend fun deleteAllEventInstanceDoubleFields()

    @Query("SELECT timestamp FROM ${EventInstanceEntity.TABLE_NAME} WHERE eventId = :eventId AND timestamp = (SELECT MAX(timestamp) FROM ${EventInstanceEntity.TABLE_NAME} WHERE eventId = :eventId)")
    suspend fun getLastInstanceTimestamp(eventId: String): DateTime?

    @Query("UPDATE ${EventInstanceEntity.TABLE_NAME} SET timestamp = :timestamp WHERE id = :instanceId")
    suspend fun updateEventInstanceTimestamp(instanceId: String, timestamp: DateTime)
}