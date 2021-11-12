package com.michasoft.thelasttime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstanceField
import com.michasoft.thelasttime.model.EventInstanceFieldSchema
import com.michasoft.thelasttime.model.EventInstanceSchema
import com.michasoft.thelasttime.model.repo.IBackupRepository
import com.michasoft.thelasttime.model.repo.IEventRepository
import com.michasoft.thelasttime.util.BackupConfig
import com.michasoft.thelasttime.util.IdGenerator
import com.michasoft.thelasttime.view.EditEventActivity.Companion.start
import com.michasoft.thelasttime.view.EventActivity.Companion.start
import com.michasoft.thelasttime.view.EventListActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import javax.inject.Inject
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var eventRepository: IEventRepository

    @Inject
    lateinit var backupRepository: IBackupRepository

    @Inject
    lateinit var backupConfig: BackupConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            eventRepository.insertEvent(event)
            Log.d("asd", "Added: " + event)
        }
    }


    fun eventList(view: View) {
        EventListActivity.start(this)
    }
}