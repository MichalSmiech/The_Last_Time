package com.michasoft.thelasttime.view.eventsList.listAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.michasoft.thelasttime.databinding.FragmentEventsListBinding
import com.michasoft.thelasttime.databinding.ListitemEventBinding
import com.michasoft.thelasttime.model.Event


/**
 * Created by mśmiech on 11.11.2020.
 */
class EventsListAdapter(private var data: List<Event>): RecyclerView.Adapter<EventsListItem>() {

    fun updateData(newData: List<Event>) {
        val diffResult = DiffUtil.calculateDiff(MyDiffCallback(this.data, newData))
        data = ArrayList(newData)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsListItem {
        val binding = ListitemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventsListItem(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: EventsListItem, position: Int) {
        holder.bindTo(data[position])
    }

    inner class MyDiffCallback(var newEvents: List<Event>, var oldEvents: List<Event>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldEvents.size
        }

        override fun getNewListSize(): Int {
            return newEvents.size
        }

        override fun areItemsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            return oldEvents[oldItemPosition].id == newEvents[newItemPosition].id
        }

        override fun areContentsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            return oldEvents[oldItemPosition] == newEvents[newItemPosition]
        }
    }
}