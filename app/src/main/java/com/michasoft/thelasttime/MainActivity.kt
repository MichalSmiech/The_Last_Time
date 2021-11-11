package com.michasoft.thelasttime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstanceField
import com.michasoft.thelasttime.model.EventInstanceFieldSchema
import com.michasoft.thelasttime.model.EventInstanceSchema
import com.michasoft.thelasttime.model.repo.IBackupRepository
import com.michasoft.thelasttime.model.repo.IEventRepository
import com.michasoft.thelasttime.util.IdGenerator
import dagger.android.AndroidInjection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var eventRepository: IEventRepository

    @Inject
    lateinit var backupRepository: IBackupRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.IO).launch {
//            addEvent()
//            addEvent()
//            addEvent()
//            val event = eventRepository.getEvent(1L)
//            Log.d("asd", "onStart: " + event)
            val time = measureTimeMillis {
                backupRepository.clearBackup()
            }
            Log.d("asd", "clearBackup: " + time)
        }
    }

    fun addEvent() {
        val event = Event(IdGenerator.autoId(), "Water plants")
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
}