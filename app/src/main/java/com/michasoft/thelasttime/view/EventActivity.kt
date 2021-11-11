package com.michasoft.thelasttime.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.michasoft.thelasttime.R
import com.michasoft.thelasttime.databinding.ActivityEventBinding
import com.michasoft.thelasttime.databinding.ActivityEventTypeBinding
import com.michasoft.thelasttime.viewModel.EventTypeViewModel
import com.michasoft.thelasttime.viewModel.EventViewModel
import dagger.android.AndroidInjection
import javax.inject.Inject

class EventActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: EventViewModel by viewModels{ viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val binding: ActivityEventBinding = DataBindingUtil.setContentView(this, R.layout.activity_event)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun onStart() {
        super.onStart()
        val eventId = ""//intent.getLongExtra(ARG_EVENT_ID, -1L)
        viewModel.setEventId(eventId)
        viewModel.flowEventBus.observe(this) {
            when(it) {
                is EventViewModel.ShowDatePicker -> {
                    //TODO show date picker
                }
            }
        }
    }

    companion object {
        const val ARG_EVENT_ID = "ARG_EVENT_ID"

        fun start(activity: AppCompatActivity, eventId: String) {
            val intent = Intent(activity, EventActivity::class.java)
            intent.putExtra(ARG_EVENT_ID, eventId)
            activity.startActivity(intent)
        }
    }
}