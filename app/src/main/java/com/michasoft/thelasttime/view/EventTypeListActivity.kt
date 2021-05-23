package com.michasoft.thelasttime.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.michasoft.thelasttime.R
import com.michasoft.thelasttime.databinding.ActivityEventTypeListBinding
import com.michasoft.thelasttime.model.EventType
import com.michasoft.thelasttime.model.repo.IEventsRepository
import com.michasoft.thelasttime.viewModel.EventTypeListViewModel
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
}