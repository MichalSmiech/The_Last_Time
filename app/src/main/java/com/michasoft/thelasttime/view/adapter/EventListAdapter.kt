package com.michasoft.thelasttime.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.michasoft.thelasttime.R
import com.michasoft.thelasttime.databinding.ListitemEventBinding
import com.michasoft.thelasttime.databinding.ListitemEventTypeBinding
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventType
import com.michasoft.thelasttime.view.viewHolder.EventTypeViewHolder
import com.michasoft.thelasttime.view.viewHolder.EventViewHolder
import com.michasoft.thelasttime.viewModel.EventTypeViewModel

/**
 * Created by mśmiech on 08.07.2021.
 */
class EventListAdapter(
    events: List<Event>,
    var viewModel: EventTypeViewModel
) : RecyclerView.Adapter<EventViewHolder>() {
    private var events = ArrayList<Event>(events)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding: ListitemEventBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.listitem_event,
            parent,
            false
        )

        return EventViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.event = event
    }

    override fun getItemCount(): Int {
        return events.size
    }

    fun setData(data: List<Event>) {
        events = ArrayList(data)
        notifyDataSetChanged()
    }
}