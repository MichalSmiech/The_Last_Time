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
import timber.log.Timber

/**
 * Created by mśmiech on 01.11.2021.
 */
class FirestoreEventSource(private val firestore: FirebaseFirestore, private val eventCollectionRef: CollectionReference) :
    IRemoteEventSource {
    override suspend fun insertEvent(event: Event) {
        requireNotNull(event.eventInstanceSchema)
        val dto = EventDto(event)
        val documentRef = eventCollectionRef.document(event.id)
        documentRef.set(dto).await()
        val fieldSchemaCollection =
            documentRef.collection(EVENT_INSTANCE_FIELD_SCHEMAS_COLLECTION_NAME)
        event.eventInstanceSchema!!.fieldSchemas.forEach { fieldSchema ->
            val fieldSchemaDto = EventInstanceFieldSchemaDto(fieldSchema)
            fieldSchemaCollection.document(fieldSchema.id).set(fieldSchemaDto).await()
        }
    }

    override suspend fun updateEvent(event: Event) {
        requireNotNull(event.eventInstanceSchema)
        val dto = EventDto(event)
        val documentRef = eventCollectionRef.document(event.id)
        documentRef.set(dto).await()
        val fieldSchemaCollection =
            documentRef.collection(EVENT_INSTANCE_FIELD_SCHEMAS_COLLECTION_NAME)
        event.eventInstanceSchema!!.fieldSchemas.forEach { fieldSchema ->
            val fieldSchemaDto = EventInstanceFieldSchemaDto(fieldSchema)
            fieldSchemaCollection.document(fieldSchema.id).set(fieldSchemaDto).await()
        }
    }

    override suspend fun getEvent(eventId: String): Event? {
        val eventDocument = eventCollectionRef.document(eventId).get().await()
        val dto = eventDocument.toObject(EventDto::class.java)
        return dto?.toModel(eventDocument.id)
    }

    override suspend fun deleteEvent(eventId: String) {
        eventCollectionRef.document(eventId).delete().await()
    }

    override suspend fun getEventInstanceSchema(eventId: String): EventInstanceSchema {
        val fieldSchemaCollection = eventCollectionRef.document(eventId)
            .collection(EVENT_INSTANCE_FIELD_SCHEMAS_COLLECTION_NAME)
        val querySnapshot = fieldSchemaCollection.get().await()
        val fieldSchemas = querySnapshot.documents.mapNotNull {
            it.toObject(EventInstanceFieldSchemaDto::class.java)?.toModel(it.id)
        }
            .sortedBy { it.order }
            .toList()
        return EventInstanceSchema(fieldSchemas)
    }

    override suspend fun insertEventInstance(instance: EventInstance) {
        val instanceMap = instance.toMap()
        val instanceCollection =
            eventCollectionRef.document(instance.eventId).collection(
                EVENT_INSTANCES_COLLECTION_NAME
            )
        instanceCollection.document(instance.id).set(instanceMap).await()
    }

    override suspend fun updateEventInstance(instance: EventInstance) {
        val instanceMap = instance.toMap()
        val instanceCollection =
            eventCollectionRef.document(instance.eventId).collection(
                EVENT_INSTANCES_COLLECTION_NAME
            )
        instanceCollection.document(instance.id).set(instanceMap).await()
    }

    override suspend fun insertEventInstances(instances: List<EventInstance>) {
        require(instances.size <= 500)
        val batch = firestore.batch()
        instances.forEach { instance ->
            val instanceMap = instance.toMap()
            val instanceCollection =
                eventCollectionRef.document(instance.eventId).collection(
                    EVENT_INSTANCES_COLLECTION_NAME
                )
            batch.set(instanceCollection.document(instance.id), instanceMap)
        }
        batch.commit().await()
    }

    override suspend fun deleteAllEvents() {
        Timber.d("Deleting all events...")
        val baseQuery = eventCollectionRef.limit(500)
        var hasNext = true
        while (hasNext) {
            val querySnapshot = baseQuery.get().await()
            if (querySnapshot.documents.size > 0) {
                querySnapshot.documents.forEach {
                    deleteAllEventInstanceSchemas(it.id)
                    deleteAllEventInstances(it.id)
                }
                val batch = firestore.batch()
                querySnapshot.documents.forEach {
                    batch.delete(it.reference)
                }
                batch.commit().await()
            } else {
                hasNext = false
            }
        }
    }

    private suspend fun deleteAllEventInstanceSchemas(eventId: String) {
        Timber.d("Deleting all event instance schemas (eventId=$eventId)...")
        val baseQuery = eventCollectionRef.document(eventId).collection(
            EVENT_INSTANCE_FIELD_SCHEMAS_COLLECTION_NAME).limit(500)
        var hasNext = true
        while (hasNext) {
            val querySnapshot = baseQuery.get().await()
            if (querySnapshot.documents.size > 0) {
                querySnapshot.documents.forEach {
                    it.reference.delete().await()
                }
            } else {
                hasNext = false
            }
        }
    }

    private suspend fun deleteAllEventInstances(eventId: String) {
        Timber.d("Deleting all event instances (eventId=$eventId)...")
        val baseQuery = eventCollectionRef.document(eventId).collection(
            EVENT_INSTANCES_COLLECTION_NAME).limit(500)
        var hasNext = true
        while (hasNext) {
            val querySnapshot = baseQuery.get().await()
            if (querySnapshot.documents.size > 0) {
                querySnapshot.documents.forEach {
                    it.reference.delete().await()
                }
            } else {
                hasNext = false
            }
        }
    }


    override suspend fun clear() {
        deleteAllEvents()
    }

    override fun getAllEvents(): Flow<Event> = flow {
        val baseQuery = eventCollectionRef.orderBy("createTimestamp").limit(100)
        var hasNext = true
        var startAfter: DocumentSnapshot? = null
        while (hasNext) {
            val querySnapshot =
                baseQuery.let { query -> startAfter?.let { query.startAfter(it) } ?: query }.get().await()
            if (querySnapshot.documents.size > 0) {
                querySnapshot.documents.forEach {
                    val eventDto = it.toObject(EventDto::class.java)
                    val event = eventDto?.toModel(it.id)
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

    override fun getAllEventInstances(eventId: String, instanceSchema: EventInstanceSchema): Flow<EventInstance> = flow {
        val baseQuery = eventCollectionRef.document(eventId).collection(
            EVENT_INSTANCE_FIELD_SCHEMAS_COLLECTION_NAME
        ).orderBy("timestamp").limit(100)
        var hasNext = true
        var startAfter: DocumentSnapshot? = null
        while (hasNext) {
            val querySnapshot =
                baseQuery.let { query -> startAfter?.let { query.startAfter(it) } ?: query }.get().await()
            if (querySnapshot.documents.size > 0) {
                querySnapshot.documents.forEach {
                    val instanceMap = it.data
                    if(instanceMap != null) {
                        val eventInstance = EventInstance(
                            eventId,
                            it.id,
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
        eventId: String,
        instanceSchema: EventInstanceSchema,
        instanceId: String
    ): EventInstance? {
        val instanceSnapshot = eventCollectionRef.document(eventId)
            .collection(EVENT_INSTANCES_COLLECTION_NAME)
            .document(instanceId)
            .get()
            .await()
        val instanceMap = instanceSnapshot.data
        instanceMap ?: return null
        return EventInstance(instanceSnapshot.id, eventId, instanceMap, instanceSchema)
    }

    override suspend fun deleteEventInstance(instance: EventInstance) {
        val instanceCollection =
            eventCollectionRef.document(instance.eventId).collection(
                EVENT_INSTANCES_COLLECTION_NAME
            )
        instanceCollection.document(instance.id).delete().await()
    }

    companion object {
        const val EVENT_INSTANCE_FIELD_SCHEMAS_COLLECTION_NAME = "eventInstanceFieldSchemas"
        const val EVENT_INSTANCES_COLLECTION_NAME = "eventInstances"
    }
}