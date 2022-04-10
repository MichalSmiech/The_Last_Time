package com.michasoft.thelasttime.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.michasoft.thelasttime.R
import com.michasoft.thelasttime.databinding.ActivityEventListBinding
import com.michasoft.thelasttime.util.IdGenerator
import com.michasoft.thelasttime.view.bottomSheet.AddEventInstanceBottomSheet
import com.michasoft.thelasttime.viewModel.EventListViewModel
import com.michasoft.thelasttime.viewModel.EventViewModel
import dagger.android.AndroidInjection
import javax.inject.Inject

class EventListActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: EventListViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val binding: ActivityEventListBinding = DataBindingUtil.setContentView(this, R.layout.activity_event_list)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun onStart() {
        super.onStart()
        viewModel.flowEventBus.observe(this) {
            when(it) {
                is EventListViewModel.CreateNewEvent -> {
                    EditEventActivity.start(this, IdGenerator.autoId())
                }
                is EventListViewModel.ShowEvent -> {
                    EventActivity.start(this, it.eventTypeId)
                }
                is EventListViewModel.ShowAddEventInstanceBottomSheet -> {
                    AddEventInstanceBottomSheet.show(supportFragmentManager, it.eventId)
                }
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        viewModel.refreshData()
    }

    companion object {
        fun start(activity: AppCompatActivity) {
            val intent = Intent(activity, EventListActivity::class.java)
            activity.startActivity(intent)
        }
    }
}