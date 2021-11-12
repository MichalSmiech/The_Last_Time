package com.michasoft.thelasttime.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.michasoft.thelasttime.R
import com.michasoft.thelasttime.databinding.ActivityEventInstanceBinding
import com.michasoft.thelasttime.viewModel.EventInstanceViewModel
import dagger.android.AndroidInjection
import javax.inject.Inject

class EventInstanceActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val instanceViewModel: EventInstanceViewModel by viewModels{ viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val binding: ActivityEventInstanceBinding = DataBindingUtil.setContentView(this, R.layout.activity_event_instance)
        binding.viewModel = instanceViewModel
        binding.lifecycleOwner = this
    }

    override fun onStart() {
        super.onStart()
        val eventId = intent.getStringExtra(ARG_EVENT_INSTANCE_ID)
        instanceViewModel.setEventId(eventId!!)
        instanceViewModel.flowEventBus.observe(this) {
            when(it) {
                is EventInstanceViewModel.ShowDatePicker -> {
                    //TODO show date picker
                }
            }
        }
    }

    companion object {
        const val ARG_EVENT_INSTANCE_ID = "EVENT_INSTANCE_ID"

        fun start(activity: AppCompatActivity, eventId: String) {
            val intent = Intent(activity, EventInstanceActivity::class.java)
            intent.putExtra(ARG_EVENT_INSTANCE_ID, eventId)
            activity.startActivity(intent)
        }
    }
}