package com.michasoft.thelasttime.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.michasoft.thelasttime.R
import com.michasoft.thelasttime.databinding.ActivityEditEventTypeBinding
import com.michasoft.thelasttime.databinding.ActivityEventTypeListBinding
import com.michasoft.thelasttime.model.EventType
import com.michasoft.thelasttime.viewModel.EditEventTypeViewModel
import com.michasoft.thelasttime.viewModel.EventTypeListViewModel
import org.joda.time.DateTime

class EditEventTypeActivity : AppCompatActivity() {

    private val viewModel: EditEventTypeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityEditEventTypeBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_event_type)
        binding.viewModel = viewModel
    }

    override fun onStart() {
        super.onStart()
        val eventTypeId = intent.getLongExtra(ARG_EVENT_TYPE_ID, -1L)

        val eventType = EventType(eventTypeId, "Plant", DateTime.now())
        viewModel.eventTypeName.value = eventType.name
    }

    companion object {
        const val ARG_EVENT_TYPE_ID = "ARG_EVENT_TYPE_ID"
    }
}