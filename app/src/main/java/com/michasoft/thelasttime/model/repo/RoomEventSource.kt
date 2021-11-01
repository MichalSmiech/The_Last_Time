package com.michasoft.thelasttime.model.repo

import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstanceField
import com.michasoft.thelasttime.model.EventInstanceField.Type
import com.michasoft.thelasttime.model.EventInstanceScheme
import com.michasoft.thelasttime.model.eventInstanceField.DoubleField
import com.michasoft.thelasttime.model.eventInstanceField.IntField
import com.michasoft.thelasttime.model.eventInstanceField.TextField
import com.michasoft.thelasttime.model.storage.dao.EventDao
import com.michasoft.thelasttime.model.storage.entity.EventEntity
import com.michasoft.thelasttime.model.storage.entity.EventInstanceEntity
import com.michasoft.thelasttime.model.storage.entity.EventInstanceFieldSchemaEntity
import com.michasoft.thelasttime.model.storage.entity.eventInstanceField.EventInstanceDoubleFieldEntity
import com.michasoft.thelasttime.model.storage.entity.eventInstanceField.EventInstanceIntFieldEntity
import com.michasoft.thelasttime.model.storage.entity.eventInstanceField.EventInstanceTextFieldEntity

/**
 * Created by mśmiech on 05.09.2021.
 */
class RoomEventSource(private val eventDao: EventDao): IEventSource {
    override suspend fun getEvent(eventId: Long): Event? {
        val eventEntity = eventDao.getEvent(eventId) ?: return null
        return eventEntity.toModel()
    }

    override suspend fun deleteEvent(eventId: Long) {
        val instanceIds = eventDao.getAllEventInstanceIdsWithEventId(eventId)
        instanceIds.forEach { instanceId ->
            deleteEventInstance(eventId, instanceId)
        }
        eventDao.deleteEventInstanceFieldSchemasWithEventId(eventId)
        eventDao.deleteEvent(eventId)
    }

    override suspend fun getEventInstanceScheme(eventId: Long): EventInstanceScheme {
        val eventInstanceFieldSchemaEntities =
            eventDao.getEventInstanceFieldSchemas(eventId).sortedBy { it.order }
        val eventInstanceFieldSchemas = eventInstanceFieldSchemaEntities.map { it.toModel() }
        return EventInstanceScheme(eventInstanceFieldSchemas)
    }

    override suspend fun getEventInstance(eventId: Long, instanceId: Long): EventInstance? {
        val eventInstanceEntity = eventDao.getEventInstance(instanceId) ?: return null
        val eventInstanceFieldSchemaEntities =
            eventDao.getEventInstanceFieldSchemas(eventInstanceEntity.eventId).sortedBy { it.order }
        val eventInstanceFields = ArrayList<EventInstanceField>()
        eventInstanceFieldSchemaEntities.forEach { fieldSchema ->
            when(fieldSchema.type) {
                Type.TextField -> {
                    val eventInstanceTextFieldEntity =
                        eventDao.getEventInstanceTextField(eventInstanceEntity.id, fieldSchema.id)
                    eventInstanceFields.add(eventInstanceTextFieldEntity.toModel())
                }
                Type.IntField -> {
                    val eventInstanceIntFieldEntity =
                        eventDao.getEventInstanceIntField(eventInstanceEntity.id, fieldSchema.id)
                    eventInstanceFields.add(eventInstanceIntFieldEntity.toModel())
                }
                Type.DoubleField -> {
                    val eventInstanceDoubleFieldEntity =
                        eventDao.getEventInstanceDoubleField(eventInstanceEntity.id, fieldSchema.id)
                    eventInstanceFields.add(eventInstanceDoubleFieldEntity.toModel())
                }
                else -> {
                    throw NotImplementedError()
                }
            }
        }
        return EventInstance(
            eventInstanceEntity.id,
            eventInstanceEntity.eventId,
            eventInstanceEntity.timestamp,
            eventInstanceFields
        )
    }

    override suspend fun getEventInstance(
        eventId: Long,
        instanceScheme: EventInstanceScheme,
        instanceId: Long
    ): EventInstance? {
        return getEventInstance(eventId, instanceId)
    }

    override suspend fun deleteEventInstance(instance: EventInstance) {
        deleteEventInstance(instance.eventId, instance.id)
    }

    private suspend fun deleteEventInstance(eventId: Long, instanceId: Long) {
        val eventInstanceFieldSchemaEntities =
            eventDao.getEventInstanceFieldSchemas(eventId)
        eventInstanceFieldSchemaEntities.distinctBy { it.type }.forEach { fieldSchema ->
            when (fieldSchema.type) {
                Type.TextField -> {
                    eventDao.deleteEventInstanceTextFieldsWithInstanceId(instanceId)
                }
                Type.IntField -> {
                    eventDao.deleteEventInstanceIntFieldsWithInstanceId(instanceId)
                }
                Type.DoubleField -> {
                    eventDao.deleteEventInstanceDoubleFieldsWithInstanceId(instanceId)
                }
                else -> {
                    throw NotImplementedError()
                }
            }
        }
        eventDao.deleteEventInstance(instanceId)
    }

    override suspend fun insertEvent(event: Event): Long {
        assert(event.eventInstanceScheme != null)
        val eventId = eventDao.insertEvent(EventEntity(event))
        event.eventInstanceScheme!!.fieldSchemas.forEach { fieldSchema ->
            fieldSchema.id = eventDao.insertEventInstanceFieldSchema(EventInstanceFieldSchemaEntity(eventId, fieldSchema))
        }
        event.id = eventId
        return eventId
    }

    override suspend fun insertEventInstance(instance: EventInstance): Long {
        val instanceId = eventDao.insertEventInstance(EventInstanceEntity(instance))
        instance.fields.forEach { field ->
            when(field.type) {
                Type.TextField -> {
                    eventDao.insertEventInstanceTextField(EventInstanceTextFieldEntity(instanceId, field as TextField))
                }
                Type.IntField -> {
                    eventDao.insertEventInstanceIntField(EventInstanceIntFieldEntity(instanceId, field as IntField))
                }
                Type.DoubleField -> {
                    eventDao.insertEventInstanceDoubleField(EventInstanceDoubleFieldEntity(instanceId, field as DoubleField))
                }
                else -> {
                    throw NotImplementedError()
                }
            }
        }
        instance.id = instanceId
        return instanceId
    }
}