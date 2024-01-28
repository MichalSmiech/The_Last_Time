package com.michasoft.thelasttime.dataSource

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.michasoft.thelasttime.model.dto.RepeatedReminderDto
import com.michasoft.thelasttime.model.dto.SingleReminderDto
import com.michasoft.thelasttime.model.reminder.Reminder
import com.michasoft.thelasttime.model.reminder.RepeatedReminder
import com.michasoft.thelasttime.model.reminder.SingleReminder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class FirestoreReminderSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    @Named("singleReminderCollectionRef") private val singleReminderCollectionRef: CollectionReference,
    @Named("repeatedReminderCollectionRef") private val repeatedReminderCollectionRef: CollectionReference,
) {
    suspend fun insertReminder(reminder: Reminder) {
        when (reminder) {
            is SingleReminder -> insertSingleReminder(reminder)
            is RepeatedReminder -> insertRepeatedReminder(reminder)
        }
    }

    private suspend fun insertSingleReminder(reminder: SingleReminder) {
        val dto = SingleReminderDto(reminder)
        val documentRef = singleReminderCollectionRef.document(reminder.id)
        documentRef.set(dto).await()
    }

    private suspend fun insertRepeatedReminder(reminder: RepeatedReminder) {
        val dto = RepeatedReminderDto(reminder)
        val documentRef = repeatedReminderCollectionRef.document(reminder.id)
        documentRef.set(dto).await()
    }

    suspend fun getAllReminders(): Flow<Reminder> = flow {
        getAllSingleReminders(this)
        getAllRepeatedReminders(this)
    }

    private suspend fun getAllSingleReminders(flowCollector: FlowCollector<Reminder>) {
        val baseQuery = singleReminderCollectionRef.orderBy("eventId").limit(100)
        var hasNext = true
        var startAfter: DocumentSnapshot? = null
        while (hasNext) {
            val querySnapshot =
                baseQuery.let { query -> startAfter?.let { query.startAfter(it) } ?: query }.get().await()
            if (querySnapshot.documents.size > 0) {
                querySnapshot.documents.forEach {
                    val eventDto = it.toObject(SingleReminderDto::class.java)
                    val event = eventDto?.toModel(it.id)
                    if (event != null) {
                        flowCollector.emit(event)
                    }
                }
                startAfter = querySnapshot.documents.last()
            } else {
                hasNext = false
            }
        }
    }

    private suspend fun getAllRepeatedReminders(flowCollector: FlowCollector<Reminder>) {
        val baseQuery = repeatedReminderCollectionRef.orderBy("eventId").limit(100)
        var hasNext = true
        var startAfter: DocumentSnapshot? = null
        while (hasNext) {
            val querySnapshot =
                baseQuery.let { query -> startAfter?.let { query.startAfter(it) } ?: query }.get().await()
            if (querySnapshot.documents.size > 0) {
                querySnapshot.documents.forEach {
                    val eventDto = it.toObject(RepeatedReminderDto::class.java)
                    val event = eventDto?.toModel(it.id)
                    if (event != null) {
                        flowCollector.emit(event)
                    }
                }
                startAfter = querySnapshot.documents.last()
            } else {
                hasNext = false
            }
        }
    }

    suspend fun deleteReminder(reminderId: String, reminderType: Reminder.Type) {
        when (reminderType) {
            Reminder.Type.Single -> singleReminderCollectionRef.document(reminderId).delete().await()
            Reminder.Type.Repeated -> repeatedReminderCollectionRef.document(reminderId).delete().await()
        }
    }

    suspend fun deleteAllReminders() {
        deleteAllSingleEvents()
        deleteAllRepeatedEvents()
    }

    private suspend fun deleteAllSingleEvents() {
        val baseQuery = singleReminderCollectionRef.limit(500)
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

    private suspend fun deleteAllRepeatedEvents() {
        val baseQuery = repeatedReminderCollectionRef.limit(500)
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

    suspend fun clear() {
        deleteAllReminders()
    }
}