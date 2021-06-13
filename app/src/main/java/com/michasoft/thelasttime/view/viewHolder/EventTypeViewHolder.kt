package com.michasoft.thelasttime.view.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.michasoft.thelasttime.databinding.ListitemEventTypeBinding
import com.michasoft.thelasttime.model.EventType
import com.michasoft.thelasttime.viewModel.EventTypeListViewModel

/**
 * Created by mśmiech on 02.05.2021.
 */
class EventTypeViewHolder(
    val binding: ListitemEventTypeBinding,
    val viewModel: EventTypeListViewModel
): RecyclerView.ViewHolder(binding.root) {
    var eventType: EventType? = null
        set(value) {
            field = value
            value?.let {
                binding.name = it.name
                binding.lastEventTimestamp = it.lastEventTimestamp?.toString() ?: ""
            }
        }
    init {
        binding.quickAddActionButton.setOnClickListener {
            eventType?.let {
                viewModel.quickAddEvent(it)
            }
        }
        binding.addActionButton.setOnClickListener {
            eventType?.let {
                viewModel.addEvent(it)
            }
        }
    }
}