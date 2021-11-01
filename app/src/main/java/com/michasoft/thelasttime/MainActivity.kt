package com.michasoft.thelasttime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstanceField
import com.michasoft.thelasttime.model.EventInstanceFieldSchema
import com.michasoft.thelasttime.model.EventInstanceSchema
import com.michasoft.thelasttime.model.repo.IEventRepository
import dagger.android.AndroidInjection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var eventRepository: IEventRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.IO).launch {
            val event = eventRepository.getEvent(1L)
//            event.eventInstanceSchema = eventRepository.
            Log.d("asd", "onStart: " + event)
        }
    }

    fun addEvent() {
        val event = Event(0L, "Water plants")
        val fieldSchemas = mutableListOf<EventInstanceFieldSchema>()
        fieldSchemas.add(EventInstanceFieldSchema(0L, 0, EventInstanceField.Type.TextField, "pierwsze pole"))
        fieldSchemas.add(EventInstanceFieldSchema(0L, 1, EventInstanceField.Type.IntField, "drugie pole"))
        fieldSchemas.add(EventInstanceFieldSchema(0L, 2, EventInstanceField.Type.DoubleField, "trzecie pole"))
        val eventInstanceSchema = EventInstanceSchema(fieldSchemas)
        event.eventInstanceSchema = eventInstanceSchema
        CoroutineScope(Dispatchers.IO).launch {
            eventRepository.insertEvent(event)
            Log.d("asd", "onStart: " + event)
        }
    }
}