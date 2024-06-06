package com.michasoft.thelasttime.dataSource

import androidx.room.withTransaction
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.EventInstanceField
import com.michasoft.thelasttime.model.EventInstanceField.Type
import com.michasoft.thelasttime.model.EventInstanceSchema
import com.michasoft.thelasttime.model.eventInstanceField.DoubleField
import com.michasoft.thelasttime.model.eventInstanceField.IntField
import com.michasoft.thelasttime.model.eventInstanceField.TextField
import com.michasoft.thelasttime.storage.AppDatabase
import com.michasoft.thelasttime.storage.dao.EventDao
import com.michasoft.thelasttime.storage.entity.EventEntity
import com.michasoft.thelasttime.storage.entity.EventInstanceEntity
import com.michasoft.thelasttime.storage.entity.EventInstanceFieldSchemaEntity
import com.michasoft.thelasttime.storage.entity.eventInstanceField.EventInstanceDoubleFieldEntity
import com.michasoft.thelasttime.storage.entity.eventInstanceField.EventInstanceIntFieldEntity
import com.michasoft.thelasttime.storage.entity.eventInstanceField.EventInstanceTextFieldEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.joda.time.DateTime

/**
 * Created by mśmiech on 05.09.2021.
 */
class RoomEventSource(
    private val appDatabase: AppDatabase,
    private val eventDao: EventDao,
) :
    ILocalEventSource {
    override suspend fun getEvent(eventId: String): Event? {
        val eventEntity = eventDao.getEvent(eventId) ?: return null
        val eventInstanceSchema = getEventInstanceSchema(eventId)
        return eventEntity.toModel(eventInstanceSchema)
    }

    override suspend fun deleteEvent(eventId: String) {
        val instanceIds = eventDao.getAllEventInstanceIdsWithEventId(eventId)
        instanceIds.forEach { instanceId ->
            deleteEventInstance(eventId, instanceId)
        }
        eventDao.deleteEventInstanceFieldSchemasWithEventId(eventId)
        eventDao.deleteEvent(eventId)
    }

    override suspend fun getEventInstanceSchema(eventId: String): EventInstanceSchema {
        val eventInstanceFieldSchemaEntities =
            eventDao.getEventInstanceFieldSchemas(eventId).sortedBy { it.order }
        val eventInstanceFieldSchemas = eventInstanceFieldSchemaEntities.map { it.toModel() }
        return EventInstanceSchema(eventInstanceFieldSchemas)
    }

    override suspend fun getEventInstance(eventId: String, instanceId: String): EventInstance? {
        val eventInstanceSchema = getEventInstanceSchema(eventId)
        return getEventInstance(eventInstanceSchema, instanceId)
    }

    override suspend fun getEventInstance(
        instanceSchema: EventInstanceSchema,
        instanceId: String
    ): EventInstance? {
        val eventInstanceEntity = eventDao.getEventInstance(instanceId) ?: return null
        val eventInstanceFields = ArrayList<EventInstanceField>(instanceSchema.fieldSchemas.size)
        instanceSchema.fieldSchemas.forEach { fieldSchema ->
            when (fieldSchema.type) {
                Type.TextField -> {
                    val eventInstanceTextFieldEntity =
                        eventDao.getEventInstanceTextField(eventInstanceEntity.id, fieldSchema.id)
                    if (eventInstanceTextFieldEntity != null) {
                        eventInstanceFields.add(eventInstanceTextFieldEntity.toModel(fieldSchema))
                    }
                }

                Type.IntField -> {
                    val eventInstanceIntFieldEntity =
                        eventDao.getEventInstanceIntField(eventInstanceEntity.id, fieldSchema.id)
                    if (eventInstanceIntFieldEntity != null) {
                        eventInstanceFields.add(eventInstanceIntFieldEntity.toModel(fieldSchema))
                    }
                }

                Type.DoubleField -> {
                    val eventInstanceDoubleFieldEntity =
                        eventDao.getEventInstanceDoubleField(eventInstanceEntity.id, fieldSchema.id)
                    if (eventInstanceDoubleFieldEntity != null) {
                        eventInstanceFields.add(eventInstanceDoubleFieldEntity.toModel(fieldSchema))
                    }
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

    override suspend fun deleteEventInstance(instance: EventInstance) {
        deleteEventInstance(instance.eventId, instance.id)
    }

    override suspend fun deleteEventInstance(eventId: String, instanceId: String) {
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

    override suspend fun insertEvent(event: Event) {
        eventDao.insertEvent(EventEntity(event))
        event.eventInstanceSchema.fieldSchemas.forEach { fieldSchema ->
            eventDao.insertEventInstanceFieldSchema(
                EventInstanceFieldSchemaEntity(
                    event.id,
                    fieldSchema
                )
            )
        }
    }

    private suspend fun deleteAllEvents() {
        eventDao.deleteAllEventInstances()
        eventDao.deleteAllEventInstanceDoubleFields()
        eventDao.deleteAllEventInstanceIntFields()
        eventDao.deleteAllEventInstanceTextFields()
        eventDao.deleteAllEventInstanceFieldSchemas()
        eventDao.deleteAllEvents()
    }

    override suspend fun clear() {
        deleteAllEvents()
    }

    override suspend fun getAllEvents(): Flow<Event> = flow {
        val offset = 0L
        val limit = 100L
        var hasNext = true
        while (hasNext) {
            val eventIds = eventDao.getEventIds(limit, offset)
            eventIds.forEach { eventId ->
                val event = getEvent(eventId)!!
                emit(event)
            }
            hasNext = eventIds.size.toLong() == limit
        }
    }

    override suspend fun getAllEventsAtOnce(): ArrayList<Event> {
        val events = ArrayList<Event>()
        val offset = 0L
        val limit = 100L
        var hasNext = true
        while (hasNext) {
            val eventIds = eventDao.getEventIdsOrderByCreateTimestamp(limit, offset)
            eventIds.forEach { eventId ->
                val event = getEvent(eventId)!!
                events.add(event)
            }
            hasNext = eventIds.size.toLong() == limit
        }
        return events
    }

    override suspend fun getAllEventInstancesAtOnce(eventId: String): ArrayList<EventInstance> {
        val eventInstanceSchema = getEventInstanceSchema(eventId)
        val instances = ArrayList<EventInstance>()
        var offset = 0L
        val limit = 100L
        var hasNext = true
        while (hasNext) {
            val instanceIds =
                eventDao.getEventInstanceIdsWithEventIdOrderByTimestamp(eventId, limit, offset)
            instanceIds.forEach { instanceId ->
                val instance = getEventInstance(eventInstanceSchema, instanceId)!!
                instances.add(instance)
            }
            hasNext = instanceIds.size.toLong() == limit
            offset += limit
        }
        return instances
    }

    override suspend fun getLastInstanceTimestamp(eventId: String): DateTime? {
        return eventDao.getLastInstanceTimestamp(eventId)
    }

    override suspend fun getAllEventInstances(
        eventId: String,
        eventInstanceSchema: EventInstanceSchema
    ): Flow<EventInstance> = flow {
        val offset = 0L
        val limit = 100L
        var hasNext = true
        while (hasNext) {
            val instanceIds = eventDao.getEventInstanceIds(limit, offset)
            instanceIds.forEach { instanceId ->
                val instance = getEventInstance(eventInstanceSchema, instanceId)!!
                emit(instance)
            }
            hasNext = instanceIds.size.toLong() == limit
        }
    }

    override suspend fun insertEventInstance(instance: EventInstance) {
        eventDao.insertEventInstance(EventInstanceEntity(instance))
        instance.fields.forEach { field ->
            when (field.type) {
                Type.TextField -> {
                    eventDao.insertEventInstanceTextField(
                        EventInstanceTextFieldEntity(
                            instance.id,
                            field as TextField
                        )
                    )
                }

                Type.IntField -> {
                    eventDao.insertEventInstanceIntField(
                        EventInstanceIntFieldEntity(
                            instance.id,
                            field as IntField
                        )
                    )
                }

                Type.DoubleField -> {
                    eventDao.insertEventInstanceDoubleField(
                        EventInstanceDoubleFieldEntity(
                            instance.id,
                            field as DoubleField
                        )
                    )
                }

                else -> {
                    throw NotImplementedError()
                }
            }
        }
    }

    override suspend fun updateEvent(event: Event) {
        appDatabase.withTransaction {
            val oldEvent = getEvent(event.id)
            if (event.name != oldEvent!!.name) {
                eventDao.updateEventName(event.id, event.name)
            }
            //TODO compare scheme and update if needed
        }
    }

    override suspend fun updateEventInstance(instance: EventInstance) {
        appDatabase.withTransaction {
            val oldInstance = getEventInstance(instance.eventId, instance.id)!!
            if (instance.timestamp != oldInstance.timestamp) {
                eventDao.updateEventInstanceTimestamp(instance.id, instance.timestamp)
            }
            //TODO compare other fields and update if needed
        }
    }
}