package com.michasoft.thelasttime.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.michasoft.thelasttime.R
import com.michasoft.thelasttime.databinding.ActivityEventTypeListBinding
import com.michasoft.thelasttime.model.EventType
import com.michasoft.thelasttime.view.adapter.EventTypeListAdapter
import com.michasoft.thelasttime.viewModel.EventTypeListViewModel
import org.joda.time.DateTime

class EventTypeListActivity : AppCompatActivity() {
    private val viewModel: EventTypeListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityEventTypeListBinding = DataBindingUtil.setContentView(this, R.layout.activity_event_type_list)

        val list = ArrayList<EventType>()
        list.add(EventType("Plants", DateTime.now()))
        list.add(EventType("Vacuum", DateTime.now().minusDays(3)))
        viewModel.eventTypes.value = list
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}