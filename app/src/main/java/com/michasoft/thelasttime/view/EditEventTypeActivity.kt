package com.michasoft.thelasttime.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.michasoft.thelasttime.R
import com.michasoft.thelasttime.databinding.ActivityEditEventTypeBinding
import com.michasoft.thelasttime.viewModel.CommonViewModel
import com.michasoft.thelasttime.viewModel.EditEventTypeViewModel
import dagger.android.AndroidInjection
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
        val eventTypeId = "";//intent.getLongExtra(ARG_EVENT_TYPE_ID, -1L)
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
    }
}