package com.michasoft.thelasttime.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.michasoft.thelasttime.R
import com.michasoft.thelasttime.databinding.ListitemEventInstanceBinding
import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.view.viewHolder.EventInstanceViewHolder
import com.michasoft.thelasttime.viewModel.EventViewModel

/**
 * Created by mśmiech on 08.07.2021.
 */
class EventInstanceListAdapter(
    eventInstances: List<EventInstance>,
    var viewModel: EventViewModel
) : RecyclerView.Adapter<EventInstanceViewHolder>() {
    private var events = ArrayList<EventInstance>(eventInstances)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventInstanceViewHolder {
        val binding: ListitemEventInstanceBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.listitem_event_instance,
            parent,
            false
        )

        return EventInstanceViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holderInstance: EventInstanceViewHolder, position: Int) {
        val event = events[position]
        holderInstance.eventInstance = event
    }

    override fun getItemCount(): Int {
        return events.size
    }

    fun setData(data: List<EventInstance>) {
        events = ArrayList(data)
        notifyDataSetChanged()
    }
}