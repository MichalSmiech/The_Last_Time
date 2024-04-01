package com.michasoft.thelasttime.dataSource

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.michasoft.thelasttime.di.UserSessionScope
import com.michasoft.thelasttime.model.Label
import com.michasoft.thelasttime.model.dto.LabelDto
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

@UserSessionScope
class FirestoreLabelSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    @Named("labelCollectionRef") private val labelCollectionRef: CollectionReference,
    @Named("eventLabelCollectionRef") private val eventLabelCollectionRef: CollectionReference,
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
        labelCollectionRef.document(labelId).delete().await()
    }
}