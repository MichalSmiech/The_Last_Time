package com.michasoft.thelasttime.model.repo

import com.google.firebase.firestore.CollectionReference
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.EventInstanceScheme
import com.michasoft.thelasttime.model.remote.dto.EventDto
import com.michasoft.thelasttime.model.remote.dto.EventInstanceFieldSchemaDto
import kotlinx.coroutines.tasks.await

/**
 * Created by mśmiech on 01.11.2021.
 */
class FirestoreEventSource(private val eventCollectionRef: CollectionReference) : IEventSource {
    override suspend fun insertEvent(event: Event): Long {
        assert(event.id != 0L)
        assert(event.eventInstanceScheme != null)
        val dto = EventDto(event)
        val documentRef = eventCollectionRef.document(event.id.toString())
        documentRef.set(dto).await()
        val fieldSchemaCollection =
            documentRef.collection(EVENT_INSTANCE_FIELD_SCHEMAS_COLLECTION_NAME)
        event.eventInstanceScheme!!.fieldSchemas.forEach { fieldSchema ->
            val fieldSchemaDto = EventInstanceFieldSchemaDto(fieldSchema)
            fieldSchemaCollection.document(fieldSchema.id.toString()).set(fieldSchemaDto).await()
        }
        return event.id
    }

    override suspend fun getEvent(eventId: Long): Event? {
        val eventDocument = eventCollectionRef.document(eventId.toString()).get().await()
        val dto = eventDocument.toObject(EventDto::class.java)
        return dto?.toModel(eventDocument.id.toLong())
    }

    override suspend fun deleteEvent(eventId: Long) {
        eventCollectionRef.document(eventId.toString()).delete().await()
    }

    override suspend fun getEventInstanceScheme(eventId: Long): EventInstanceScheme {
        val fieldSchemaCollection = eventCollectionRef.document(eventId.toString())
            .collection(EVENT_INSTANCE_FIELD_SCHEMAS_COLLECTION_NAME)
        val querySnapshot = fieldSchemaCollection.get().await()
        val fieldSchemas = querySnapshot.documents.mapNotNull {
            it.toObject(EventInstanceFieldSchemaDto::class.java)?.toModel(it.id.toLong())
        }
            .sortedBy { it.order }
            .toList()
        return EventInstanceScheme(fieldSchemas)
    }

    override suspend fun insertEventInstance(instance: EventInstance): Long {
        assert(instance.id != 0L)
        val instanceMap = instance.toMap()
        val instanceCollection =
            eventCollectionRef.document(instance.eventId.toString()).collection(
                EVENT_INSTANCES_COLLECTION_NAME
            )
        instanceCollection.document(instance.id.toString()).set(instanceMap).await()
        return instance.id
    }

    override suspend fun getEventInstance(eventId: Long, instanceId: Long): EventInstance? {
        val instanceScheme = getEventInstanceScheme(eventId)
        return getEventInstance(eventId, instanceScheme, instanceId)
    }

    override suspend fun getEventInstance(
        eventId: Long,
        instanceScheme: EventInstanceScheme,
        instanceId: Long
    ): EventInstance? {
        val instanceSnapshot = eventCollectionRef.document(eventId.toString())
            .collection(EVENT_INSTANCES_COLLECTION_NAME)
            .document(instanceId.toString())
            .get()
            .await()
        val instanceMap = instanceSnapshot.data
        instanceMap ?: return null
        return EventInstance(instanceSnapshot.id.toLong(), instanceMap, instanceScheme)
    }

    override suspend fun deleteEventInstance(instance: EventInstance) {
        val instanceCollection =
            eventCollectionRef.document(instance.eventId.toString()).collection(
                EVENT_INSTANCES_COLLECTION_NAME
            )
        instanceCollection.document(instance.id.toString()).delete().await()
    }

    companion object {
        const val EVENT_INSTANCE_FIELD_SCHEMAS_COLLECTION_NAME = "eventInstanceFieldSchemas"
        const val EVENT_INSTANCES_COLLECTION_NAME = "eventInstances"
    }
}