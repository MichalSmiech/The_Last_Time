package com.michasoft.thelasttime.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.michasoft.thelasttime.LastTimeApplication
import com.michasoft.thelasttime.R
import com.michasoft.thelasttime.databinding.ActivityEditEventBinding
import com.michasoft.thelasttime.viewModel.CommonViewModel
import com.michasoft.thelasttime.viewModel.EditEventViewModel
import javax.inject.Inject

class EditEventActivity : UserSessionActivity() {
    @Inject
    lateinit var viewModelFactory: EditEventViewModel.Factory
    private val viewModel: EditEventViewModel by viewModels{ viewModelFactory }

    override fun onActivityCreate(savedInstanceState: Bundle?) {
        (application as LastTimeApplication).userSessionComponent!!.inject(this)
        val binding: ActivityEditEventBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_event)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun onStart() {
        super.onStart()
        val eventId = intent.getStringExtra(ARG_EVENT_ID)
        viewModel.start(eventId!!)
        viewModel.flowEventBus.observe(this) {
            when(it) {
                is CommonViewModel.Finish -> {
                    finishAfterTransition()
                }
            }
        }
    }

    companion object {
        const val ARG_EVENT_ID = "ARG_EVENT_ID"

        fun start(activity: AppCompatActivity, eventId: String) {
            val intent = Intent(activity, EditEventActivity::class.java)
            intent.putExtra(ARG_EVENT_ID, eventId)
            activity.startActivity(intent)
        }
    }
}