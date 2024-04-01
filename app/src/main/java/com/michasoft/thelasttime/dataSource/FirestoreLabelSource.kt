package com.michasoft.thelasttime.dataSource

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.michasoft.thelasttime.di.UserSessionScope
import com.michasoft.thelasttime.model.Label
import com.michasoft.thelasttime.model.dto.EventLabelDto
import com.michasoft.thelasttime.model.dto.LabelDto
import com.michasoft.thelasttime.util.deleteAllDocuments
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
}