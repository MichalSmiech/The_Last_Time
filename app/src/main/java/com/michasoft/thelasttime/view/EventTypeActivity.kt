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
import com.michasoft.thelasttime.databinding.ActivityEventTypeBinding
import com.michasoft.thelasttime.viewModel.CommonViewModel
import com.michasoft.thelasttime.viewModel.EditEventTypeViewModel
import com.michasoft.thelasttime.viewModel.EventTypeViewModel
import dagger.android.AndroidInjection
import javax.inject.Inject

class EventTypeActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: EventTypeViewModel by viewModels{ viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val binding: ActivityEventTypeBinding = DataBindingUtil.setContentView(this, R.layout.activity_event_type)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun onStart() {
        super.onStart()
        val eventTypeId = intent.getLongExtra(ARG_EVENT_TYPE_ID, -1L)
        viewModel.start(eventTypeId)
        viewModel.flowEventBus.observe(this) {
            when(it) {
                is CommonViewModel.Finish -> {
                    finish()
                }
            }
        }
    }

    companion object {
        const val ARG_EVENT_TYPE_ID = "ARG_EVENT_TYPE_ID"

        fun start(activity: AppCompatActivity, eventTypeId: Long) {
            val intent = Intent(activity, EventTypeActivity::class.java)
            intent.putExtra(ARG_EVENT_TYPE_ID, eventTypeId)
            activity.startActivity(intent)
        }
    }
}