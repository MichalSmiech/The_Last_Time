package com.michasoft.thelasttime.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.michasoft.thelasttime.R
import com.michasoft.thelasttime.databinding.ActivityEditEventTypeBinding
import com.michasoft.thelasttime.databinding.ActivityEventTypeListBinding
import com.michasoft.thelasttime.model.EventType
import com.michasoft.thelasttime.viewModel.EditEventTypeViewModel
import com.michasoft.thelasttime.viewModel.EventTypeListViewModel
import dagger.android.AndroidInjection
import org.joda.time.DateTime
import javax.inject.Inject

class EditEventTypeActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: EditEventTypeViewModel by viewModels{ viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val binding: ActivityEditEventTypeBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_event_type)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun onStart() {
        super.onStart()
        val eventTypeId = intent.getLongExtra(ARG_EVENT_TYPE_ID, -1L)
        viewModel.start(eventTypeId)
        viewModel.flowEventBus.observe(this) {
            when(it) {
                is EditEventTypeViewModel.Finish -> {
                    finish()
                }
            }
        }
    }

    companion object {
        const val ARG_EVENT_TYPE_ID = "ARG_EVENT_TYPE_ID"
    }
}