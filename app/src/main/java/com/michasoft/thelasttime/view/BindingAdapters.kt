package com.michasoft.thelasttime.view

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventType
import com.michasoft.thelasttime.view.adapter.EventListAdapter
import com.michasoft.thelasttime.view.adapter.EventTypeListAdapter
import com.michasoft.thelasttime.viewModel.EditEventTypeViewModel
import com.michasoft.thelasttime.viewModel.EventTypeListViewModel
import com.michasoft.thelasttime.viewModel.EventTypeViewModel

/**
 * Created by mśmiech on 02.05.2021.
 */

@BindingAdapter(value = ["eventTypes", "viewModel"], requireAll = true)
fun RecyclerView.setEventTypes(eventTypes: List<EventType>, viewModel: EventTypeListViewModel) {
    if (this.adapter == null) {
        this.adapter = EventTypeListAdapter(eventTypes, viewModel)
    } else {
        (this.adapter as EventTypeListAdapter).setData(eventTypes)
        (this.adapter as EventTypeListAdapter).viewModel = viewModel
    }
}

@BindingAdapter(value = ["eventTypes", "viewModel"], requireAll = true)
fun RecyclerView.setEvents(events: List<Event>, viewModel: EventTypeViewModel) {
    if (this.adapter == null) {
        this.adapter = EventListAdapter(events, viewModel)
    } else {
        (this.adapter as EventListAdapter).setData(events)
        (this.adapter as EventListAdapter).viewModel = viewModel
    }
}