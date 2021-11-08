package com.michasoft.thelasttime.model.dataSource

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.EventInstanceSchema
import com.michasoft.thelasttime.model.remote.dto.EventDto
import com.michasoft.thelasttime.model.remote.dto.EventInstanceFieldSchemaDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

/**
 * Created by mśmiech on 01.11.2021.
 */
class FirestoreEventSource(private val firestore: FirebaseFirestore, private val eventCollectionRef: CollectionReference) :
    IRemoteEventSource {
    override suspend fun insertEvent(event: Event): Long {
        require(event.id != 0L)
        requireNotNull(event.eventInstanceSchema)
        val dto = EventDto(event)
        val documentRef = eventCollectionRef.document(event.id.toString())
        documentRef.set(dto).await()
        val fieldSchemaCollection =
            documentRef.collection(EVENT_INSTANCE_FIELD_SCHEMAS_COLLECTION_NAME)
        event.eventInstanceSchema!!.fieldSchemas.forEach { fieldSchema ->
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

    override suspend fun getEventInstanceSchema(eventId: Long): EventInstanceSchema {
        val fieldSchemaCollection = eventCollectionRef.document(eventId.toString())
            .collection(EVENT_INSTANCE_FIELD_SCHEMAS_COLLECTION_NAME)
        val querySnapshot = fieldSchemaCollection.get().await()
        val fieldSchemas = querySnapshot.documents.mapNotNull {
            it.toObject(EventInstanceFieldSchemaDto::class.java)?.toModel(it.id.toLong())
        }
            .sortedBy { it.order }
            .toList()
        return EventInstanceSchema(fieldSchemas)
    }

    override suspend fun insertEventInstance(instance: EventInstance): Long {
        require(instance.id != 0L)
        val instanceMap = instance.toMap()
        val instanceCollection =
            eventCollectionRef.document(instance.eventId.toString()).collection(
                EVENT_INSTANCES_COLLECTION_NAME
            )
        instanceCollection.document(instance.id.toString()).set(instanceMap).await()
        return instance.id
    }

    override suspend fun insertEventInstances(instances: List<EventInstance>) {
        require(instances.size <= 500)
        val batch = firestore.batch()
        instances.forEach { instance ->
            require(instance.id != 0L)
            val instanceMap = instance.toMap()
            val instanceCollection =
                eventCollectionRef.document(instance.eventId.toString()).collection(
                    EVENT_INSTANCES_COLLECTION_NAME
                )
            batch.set(instanceCollection.document(instance.id.toString()), instanceMap)
        }
        batch.commit().await()
    }

    override suspend fun deleteAllEvents() {
        val baseQuery = eventCollectionRef.limit(500)
        var hasNext = true
        var batch = firestore.batch()
        var count = 0
        while (hasNext) {
            val querySnapshot =
                baseQuery.get().await()
            if (querySnapshot.documents.size > 0) {
                querySnapshot.documents.forEach {
                    batch.delete(it.reference)
                    count += 1
                    if (count == 500) {
                        batch.commit().await()
                        batch = firestore.batch()
                        count = 0
                    }
                }
            } else {
                hasNext = false
            }
        }
        if (count > 500) {
            batch.commit().await()
            batch = firestore.batch()
            count = 0
        }
    }

    override suspend fun clear() {
        deleteAllEvents()
    }

    override fun getAllEvents(): Flow<Event> = flow {
        val baseQuery = eventCollectionRef.orderBy("displayName").limit(100)
        var hasNext = true
        var startAfter: DocumentSnapshot? = null
        while (hasNext) {
            val querySnapshot =
                baseQuery.apply { startAfter?.let { startAfter(it) } }.get().await()
            if (querySnapshot.documents.size > 0) {
                querySnapshot.documents.forEach {
                    val eventDto = it.toObject(EventDto::class.java)
                    val event = eventDto?.toModel(it.id.toLong())
                    if (event != null) {
                        emit(event)
                    }
                }
                startAfter = querySnapshot.documents.last()
            } else {
                hasNext = false
            }
        }
    }

    override fun getAllEventInstances(eventId: Long, instanceSchema: EventInstanceSchema): Flow<EventInstance> = flow {
        val baseQuery = eventCollectionRef.document(eventId.toString()).collection(
            EVENT_INSTANCE_FIELD_SCHEMAS_COLLECTION_NAME
        ).orderBy("timestamp").limit(100)
        var hasNext = true
        var startAfter: DocumentSnapshot? = null
        while (hasNext) {
            val querySnapshot =
                baseQuery.apply { startAfter?.let { startAfter(it) } }.get().await()
            if (querySnapshot.documents.size > 0) {
                querySnapshot.documents.forEach {
                    val instanceMap = it.data
                    if(instanceMap != null) {
                        val eventInstance = EventInstance(
                            eventId,
                            it.id.toLong(),
                            instanceMap,
                            instanceSchema
                        )
                        emit(eventInstance)
                    }
                }
                startAfter = querySnapshot.documents.last()
            } else {
                hasNext = false
            }
        }
    }

    override suspend fun getEventInstance(
        eventId: Long,
        instanceSchema: EventInstanceSchema,
        instanceId: Long
    ): EventInstance? {
        val instanceSnapshot = eventCollectionRef.document(eventId.toString())
            .collection(EVENT_INSTANCES_COLLECTION_NAME)
            .document(instanceId.toString())
            .get()
            .await()
        val instanceMap = instanceSnapshot.data
        instanceMap ?: return null
        return EventInstance(eventId, instanceSnapshot.id.toLong(), instanceMap, instanceSchema)
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