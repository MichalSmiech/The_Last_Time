package com.michasoft.thelasttime.view

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.view.adapter.EventInstanceListAdapter
import com.michasoft.thelasttime.view.adapter.EventListAdapter
import com.michasoft.thelasttime.viewModel.EventListViewModel
import com.michasoft.thelasttime.viewModel.EventViewModel

/**
 * Created by mśmiech on 02.05.2021.
 */

@BindingAdapter(value = ["eventTypes", "viewModel"], requireAll = true)
fun RecyclerView.setEventTypes(events: List<Event>, viewModel: EventListViewModel) {
    if (this.adapter == null) {
        this.adapter = EventListAdapter(events, viewModel)
    } else {
        (this.adapter as EventListAdapter).setData(events)
        (this.adapter as EventListAdapter).viewModel = viewModel
    }
}

@BindingAdapter(value = ["eventTypes", "viewModel"], requireAll = true)
fun RecyclerView.setEvents(eventInstances: List<EventInstance>, viewModel: EventViewModel) {
    if (this.adapter == null) {
        this.adapter = EventInstanceListAdapter(eventInstances, viewModel)
    } else {
        (this.adapter as EventInstanceListAdapter).setData(eventInstances)
        (this.adapter as EventInstanceListAdapter).viewModel = viewModel
    }
}