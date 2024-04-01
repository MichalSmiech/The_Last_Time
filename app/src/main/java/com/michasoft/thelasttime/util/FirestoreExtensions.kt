package com.michasoft.thelasttime.util

import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await

suspend fun CollectionReference.deleteAllDocuments() {
    val baseQuery = this.limit(500)
    var hasNext = true
    while (hasNext) {
        val querySnapshot = baseQuery.get().await()
        if (querySnapshot.documents.size > 0) {
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