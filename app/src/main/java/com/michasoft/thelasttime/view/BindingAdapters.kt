package com.michasoft.thelasttime.view

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.michasoft.thelasttime.model.EventType
import com.michasoft.thelasttime.view.adapter.EventTypeListAdapter

/**
 * Created by mśmiech on 02.05.2021.
 */

@BindingAdapter("eventTypes")
fun RecyclerView.setEventTypes(eventTypes: List<EventType>) {
    if (this.adapter == null) {
        this.adapter = EventTypeListAdapter(eventTypes)
    } else {
        (this.adapter as EventTypeListAdapter).setData(eventTypes)
    }
}