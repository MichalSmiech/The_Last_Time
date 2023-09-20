package com.michasoft.thelasttime.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.michasoft.thelasttime.LastTimeApplication
import com.michasoft.thelasttime.R
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstanceField
import com.michasoft.thelasttime.model.EventInstanceFieldSchema
import com.michasoft.thelasttime.model.EventInstanceSchema
import com.michasoft.thelasttime.model.repo.IBackupRepository
import com.michasoft.thelasttime.model.repo.IEventRepository
import com.michasoft.thelasttime.model.repo.UserSessionRepository
import com.michasoft.thelasttime.util.BackupConfig
import com.michasoft.thelasttime.util.IdGenerator
import kotlinx.coroutines.*
import org.joda.time.DateTime
import javax.inject.Inject

class MainActivity : UserSessionActivity() {
    @Inject
    lateinit var eventRepository: IEventRepository

    @Inject
    lateinit var backupRepository: IBackupRepository

    @Inject
    lateinit var backupConfig: BackupConfig

    @Inject lateinit var userSessionRepository: UserSessionRepository

    override fun onActivityCreate(savedInstanceState: Bundle?) {
        (application as LastTimeApplication).userSessionComponent!!.inject(this)
        setContentView(R.layout.activity_main)
        val switch1 = findViewById<com.google.android.material.switchmaterial.SwitchMaterial>(R.id.switch1)
        CoroutineScope(Dispatchers.IO).launch {
            val autoBackup = backupConfig.isAutoBackup()
            withContext(Dispatchers.Main) {
                switch1.isChecked = autoBackup
            }
        }

        switch1.setOnCheckedChangeListener { buttonView, isChecked ->
            CoroutineScope(Dispatchers.IO).launch {
                backupConfig.setAutoBackup(isChecked)
            }
        }
    }

    fun clearLocal(view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            backupRepository.clearLocalDatabase()
        }
    }

    fun restoreBackup(view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            backupRepository.restoreBackup()
        }
    }

    fun addEvents(view: View) {
        val event = Event(IdGenerator.autoId(), "Water plants", DateTime.now())
        val fieldSchemas = mutableListOf<EventInstanceFieldSchema>()
        fieldSchemas.add(
            EventInstanceFieldSchema(
                IdGenerator.autoId(),
                0,
                EventInstanceField.Type.TextField,
                "pierwsze pole"
            )
        )
        fieldSchemas.add(
            EventInstanceFieldSchema(
                IdGenerator.autoId(),
                1,
                EventInstanceField.Type.IntField,
                "drugie pole"
            )
        )
        fieldSchemas.add(
            EventInstanceFieldSchema(
                IdGenerator.autoId(),
                2,
                EventInstanceField.Type.DoubleField,
                "trzecie pole"
            )
        )
        val eventInstanceSchema = EventInstanceSchema(fieldSchemas)
        event.eventInstanceSchema = eventInstanceSchema
        CoroutineScope(Dispatchers.IO).launch {
            eventRepository.insert(event)
            Log.d("asd", "Added: " + event)
        }
    }


    fun eventList(view: View) {
        EventListActivity.start(this)
    }

    fun logout(view: View) {
        runBlocking {
            userSessionRepository.logout()
        }
        Intent(this, LoginActivity::class.java).also {
            startActivity(it)
        }
        finish()
    }
}