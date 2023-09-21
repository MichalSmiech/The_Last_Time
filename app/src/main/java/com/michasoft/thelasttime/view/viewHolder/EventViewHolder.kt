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
                binding.name = it.name
                binding.lastInstanceTimestamp = it.lastInstanceTimestamp?.toString() ?: ""
            }
        }
    init {
        binding.addActionButton.setOnClickListener {
            event?.let {
                viewModel.addEventInstance(it)
            }
        }
        binding.listitemEventLayout.setOnClickListener {
            event?.let {
                viewModel.showEventType(it)
            }
        }
    }
}