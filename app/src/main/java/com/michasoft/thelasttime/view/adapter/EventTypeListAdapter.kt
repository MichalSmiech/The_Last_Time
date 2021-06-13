package com.michasoft.thelasttime.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.michasoft.thelasttime.R
import com.michasoft.thelasttime.databinding.ListitemEventTypeBinding
import com.michasoft.thelasttime.model.EventType
import com.michasoft.thelasttime.view.viewHolder.EventTypeViewHolder
import com.michasoft.thelasttime.viewModel.EventTypeListViewModel

/**
 * Created by mśmiech on 02.05.2021.
 */
class EventTypeListAdapter(
    eventTypes: List<EventType>,
    var viewModel: EventTypeListViewModel
) :
    RecyclerView.Adapter<EventTypeViewHolder>() {
    private var eventTypes = ArrayList<EventType>(eventTypes)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventTypeViewHolder {
        val binding: ListitemEventTypeBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.listitem_event_type,
            parent,
            false
        )

        return EventTypeViewHolder(binding, viewModel)
    }

    override fun getItemCount(): Int {
        return eventTypes.size
    }

    override fun onBindViewHolder(holder: EventTypeViewHolder, position: Int) {
        val eventType = eventTypes[position]
        holder.eventType = eventType
    }

    fun setData(data: List<EventType>) {
        eventTypes = ArrayList(data)
        notifyDataSetChanged()
    }
}