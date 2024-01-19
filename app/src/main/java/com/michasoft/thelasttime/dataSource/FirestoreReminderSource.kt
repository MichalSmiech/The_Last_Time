package com.michasoft.thelasttime.dataSource

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.michasoft.thelasttime.model.reminder.Reminder
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class FirestoreReminderSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    @Named("reminderCollectionRef") private val reminderCollectionRef: CollectionReference
) {
    suspend fun insertReminder(reminder: Reminder) {

    }

    suspend fun getAllReminders(): Flow<Reminder> {
        TODO()
    }

    suspend fun deleteReminder(reminder: Reminder) {

    }

    suspend fun deleteAllEvents() {

    }
}