package com.michasoft.thelasttime.view.eventsList.listAdapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.michasoft.thelasttime.databinding.ListitemEventBinding
import com.michasoft.thelasttime.model.Event

/**
 * Created by mśmiech on 11.11.2020.
 */

class EventsListItem (val binding: ListitemEventBinding) : RecyclerView.ViewHolder(binding.root) {
    var event: Event? = null

    fun bindTo(event: Event) {
        this.event = event
        binding.eventName = event.name
    }
}