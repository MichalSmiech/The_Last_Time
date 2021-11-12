package com.michasoft.thelasttime.view.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.michasoft.thelasttime.databinding.ListitemEventBinding
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.viewModel.EventListViewModel

/**
 * Created by mśmiech on 02.05.2021.
 */
class EventViewHolder(
    val binding: ListitemEventBinding,
    val viewModel: EventListViewModel
): RecyclerView.ViewHolder(binding.root) {
    var event: Event? = null
        set(value) {
            field = value
            value?.let {
                binding.name = it.displayName
                binding.lastEventTimestamp = it.lastEventTimestamp?.toString() ?: ""
            }
        }
    init {
        binding.quickAddActionButton.setOnClickListener {
            event?.let {
                viewModel.quickAddEventInstance(it)
            }
        }
        binding.addActionButton.setOnClickListener {
            event?.let {
                viewModel.addEvent(it)
            }
        }
        binding.listitemEventTypeLayout.setOnClickListener {
            event?.let {
                viewModel.showEventType(it)
            }
        }
    }
}