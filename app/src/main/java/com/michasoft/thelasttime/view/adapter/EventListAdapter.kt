package com.michasoft.thelasttime.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.michasoft.thelasttime.R
import com.michasoft.thelasttime.databinding.ListitemEventBinding
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.view.viewHolder.EventViewHolder
import com.michasoft.thelasttime.viewModel.EventListViewModel

/**
 * Created by mśmiech on 02.05.2021.
 */
class EventListAdapter(
    events: List<Event>,
    var viewModel: EventListViewModel
) :
    RecyclerView.Adapter<EventViewHolder>() {
    private var eventTypes = ArrayList<Event>(events)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding: ListitemEventBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.listitem_event,
            parent,
            false
        )

        return EventViewHolder(binding, viewModel)
    }

    override fun getItemCount(): Int {
        return eventTypes.size
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val eventType = eventTypes[position]
        holder.event = eventType
    }

    fun setData(data: List<Event>) {
        eventTypes = ArrayList(data)
        notifyDataSetChanged()
    }
}