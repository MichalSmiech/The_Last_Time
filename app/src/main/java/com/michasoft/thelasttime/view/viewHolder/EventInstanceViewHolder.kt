package com.michasoft.thelasttime.view.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.michasoft.thelasttime.databinding.ListitemEventInstanceBinding
import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.viewModel.EventViewModel
import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.format.PeriodFormatter
import org.joda.time.format.PeriodFormatterBuilder

/**
 * Created by mśmiech on 08.07.2021.
 */
class EventInstanceViewHolder(
    val binding: ListitemEventInstanceBinding,
    val viewModel: EventViewModel
) : RecyclerView.ViewHolder(binding.root) {
    var eventInstance: EventInstance? = null
        set(value) {
            field = value
            value?.let {
                binding.timestamp = it.timestamp.toString("E, dd MMM yyyy HH:mm")
                if(it.timestamp.isBeforeNow) {
                    val period = Period(it.timestamp, DateTime.now())
                    binding.period = period.toString(periodFormatter) + " ago"
                } else {
                    val period = Period(DateTime.now(), it.timestamp)
                    binding.period = period.toString(periodFormatter) + " until"
                }

            }
        }

    init {
        binding.listitemEventLayout.setOnClickListener {
            viewModel.showEvent(eventInstance!!)
        }
    }

    companion object {
        val periodFormatter: PeriodFormatter = PeriodFormatterBuilder()
            .appendYears().appendSuffix(" year", " years")
            .appendSeparator(", ")
            .appendMonths().appendSuffix(" month", " months")
            .appendSeparator(", ")
            .appendDays().appendSuffix(" day", " days")
            .appendSeparator(", ")
            .appendHours().appendSuffix(" hour", " hours")
            .appendSeparator(", ")
            .appendMinutes().appendSuffix(" minute", " minutes")
            .toFormatter()
    }
}