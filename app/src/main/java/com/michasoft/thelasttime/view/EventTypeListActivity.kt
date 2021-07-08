package com.michasoft.thelasttime.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.michasoft.thelasttime.R
import com.michasoft.thelasttime.databinding.ActivityEventTypeListBinding
import com.michasoft.thelasttime.model.EventType
import com.michasoft.thelasttime.model.repo.IEventsRepository
import com.michasoft.thelasttime.viewModel.EventTypeListViewModel
import com.michasoft.thelasttime.viewModel.EventTypeViewModel
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import org.joda.time.DateTime
import javax.inject.Inject

class EventTypeListActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: EventTypeListViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val binding: ActivityEventTypeListBinding = DataBindingUtil.setContentView(this, R.layout.activity_event_type_list)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun onStart() {
        super.onStart()
        viewModel.flowEventBus.observe(this) {
            when(it) {
                is EventTypeListViewModel.CreateNewEventType -> {
                    val intent = Intent(this, EditEventTypeActivity::class.java)
                    startActivity(intent)
                }
                is EventTypeListViewModel.ShowEventType -> {
                    EventTypeActivity.start(this, it.eventTypeId)
                }
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        viewModel.refreshData()
    }
}