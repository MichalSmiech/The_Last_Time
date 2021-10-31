package com.michasoft.thelasttime.view.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.michasoft.thelasttime.databinding.ListitemEventBinding
import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.viewModel.EventTypeViewModel

/**
 * Created by mśmiech on 08.07.2021.
 */
class EventViewHolder(
    val binding: ListitemEventBinding,
    val viewModel: EventTypeViewModel
): RecyclerView.ViewHolder(binding.root) {
    var eventInstance: EventInstance? = null
        set(value) {
            field = value
            value?.let {
                binding.timestamp = it.timestamp.toString()
            }
        }
    init {
        binding.listitemEventLayout.setOnClickListener {
            viewModel.showEvent(eventInstance!!)
        }
    }
}