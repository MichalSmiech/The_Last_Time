package com.michasoft.thelasttime.dataSource

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.michasoft.thelasttime.di.UserSessionScope
import com.michasoft.thelasttime.model.Label
import com.michasoft.thelasttime.model.dto.EventLabelDto
import com.michasoft.thelasttime.model.dto.LabelDto
import com.michasoft.thelasttime.util.deleteAllDocuments
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

private const val EVENT_LABEL_COLLECTION = "events"

@UserSessionScope
class FirestoreLabelSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    @Named("labelCollectionRef") private val labelCollectionRef: CollectionReference,
) {
    suspend fun insertLabel(label: Label) {
        setLabel(label)
    }

    suspend fun updateLabel(label: Label) {
        setLabel(label)
    }

    private suspend fun setLabel(label: Label) {
        val dto = LabelDto(label)
        val documentRef = labelCollectionRef.document(label.id)
        documentRef.set(dto).await()
    }

    suspend fun deleteLabel(labelId: String) {
        deleteAllEventLabels(labelId)
        labelCollectionRef.document(labelId).delete().await()
    }

    suspend fun insertEventLabel(eventId: String, labelId: String) {
        val documentRef = labelCollectionRef.document(labelId).collection(EVENT_LABEL_COLLECTION).document(eventId)
        documentRef.set(EventLabelDto(eventId)).await()
    }

    suspend fun deleteEventLabel(eventId: String, labelId: String) {
        labelCollectionRef.document(labelId).collection(EVENT_LABEL_COLLECTION).document(eventId).delete()
    }

    private suspend fun deleteAllEventLabels(labelId: String) {
        labelCollectionRef.document(labelId).collection(EVENT_LABEL_COLLECTION).deleteAllDocuments()
    }

    suspend fun getAllLabels(): Flow<Label> = flow {
        val baseQuery = labelCollectionRef.orderBy("name").limit(100)
        var hasNext = true
        var startAfter: DocumentSnapshot? = null
        while (hasNext) {
            val querySnapshot =
                baseQuery.let { query -> startAfter?.let { query.startAfter(it) } ?: query }.get().await()
            if (querySnapshot.documents.size > 0) {
                querySnapshot.documents.forEach {
                    val labelDto = it.toObject(LabelDto::class.java)
                    val label = labelDto?.toModel(it.id)
                    if (label != null) {
                        emit(label)
                    }
                }
                startAfter = querySnapshot.documents.last()
            } else {
                hasNext = false
            }
        }
    }


    /**
     * @return flow of event ids
     */
    fun getEventLabels(labelId: String): Flow<String> = flow {
        val baseQuery =
            labelCollectionRef.document(labelId).collection(EVENT_LABEL_COLLECTION).orderBy("eventId").limit(100)
        var hasNext = true
        var startAfter: DocumentSnapshot? = null
        while (hasNext) {
            val querySnapshot =
                baseQuery.let { query -> startAfter?.let { query.startAfter(it) } ?: query }.get().await()
            if (querySnapshot.documents.size > 0) {
                querySnapshot.documents.forEach {
                    emit(it.id)
                }
                startAfter = querySnapshot.documents.last()
            } else {
                hasNext = false
            }
        }
    }
}