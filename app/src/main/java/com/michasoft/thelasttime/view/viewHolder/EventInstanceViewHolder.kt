package com.michasoft.thelasttime.view.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.michasoft.thelasttime.databinding.ListitemEventInstanceBinding
import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.viewModel.EventViewModel

/**
 * Created by mśmiech on 08.07.2021.
 */
class EventInstanceViewHolder(
    val binding: ListitemEventInstanceBinding,
    val viewModel: EventViewModel
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