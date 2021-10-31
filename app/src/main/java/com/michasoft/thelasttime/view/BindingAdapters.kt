package com.michasoft.thelasttime.view

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.view.adapter.EventListAdapter
import com.michasoft.thelasttime.view.adapter.EventTypeListAdapter
import com.michasoft.thelasttime.viewModel.EventTypeListViewModel
import com.michasoft.thelasttime.viewModel.EventTypeViewModel

/**
 * Created by mśmiech on 02.05.2021.
 */

@BindingAdapter(value = ["eventTypes", "viewModel"], requireAll = true)
fun RecyclerView.setEventTypes(events: List<Event>, viewModel: EventTypeListViewModel) {
    if (this.adapter == null) {
        this.adapter = EventTypeListAdapter(events, viewModel)
    } else {
        (this.adapter as EventTypeListAdapter).setData(events)
        (this.adapter as EventTypeListAdapter).viewModel = viewModel
    }
}

@BindingAdapter(value = ["eventTypes", "viewModel"], requireAll = true)
fun RecyclerView.setEvents(eventInstances: List<EventInstance>, viewModel: EventTypeViewModel) {
    if (this.adapter == null) {
        this.adapter = EventListAdapter(eventInstances, viewModel)
    } else {
        (this.adapter as EventListAdapter).setData(eventInstances)
        (this.adapter as EventListAdapter).viewModel = viewModel
    }
}