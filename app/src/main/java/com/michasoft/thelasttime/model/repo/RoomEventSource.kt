package com.michasoft.thelasttime.model.repo

import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstanceField
import com.michasoft.thelasttime.model.EventInstanceField.Type
import com.michasoft.thelasttime.model.eventInstanceField.DoubleField
import com.michasoft.thelasttime.model.eventInstanceField.IntField
import com.michasoft.thelasttime.model.eventInstanceField.TextField
import com.michasoft.thelasttime.model.storage.dao.EventDao
import com.michasoft.thelasttime.model.storage.entity.EventInstanceEntity
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

    override suspend fun getEventInstance(instanceId: Long): EventInstance? {
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

    override suspend fun insertEvent(event: Event): Long {
        TODO("Not yet implemented")
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
        return instanceId
    }

    override suspend fun saveEventInstance(instance: EventInstance) {
        TODO("Not yet implemented")
    }

    override suspend fun saveEvent(event: Event) {
        TODO("Not yet implemented")
    }
}