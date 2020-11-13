package com.michasoft.thelasttime.view.eventsList

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.michasoft.thelasttime.R
import com.michasoft.thelasttime.databinding.FragmentEventsListBinding
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.view.eventsList.listAdapter.EventsListAdapter

class EventsListFragment : Fragment() {
    private lateinit var binding: FragmentEventsListBinding
    companion object {
        fun newInstance() = EventsListFragment()

        @JvmStatic
        @BindingAdapter("events")
        fun setEventsListData(list: RecyclerView, data: List<Event>) {
            var adapter: EventsListAdapter? = list.adapter as EventsListAdapter?
            if(adapter == null) {
                adapter = EventsListAdapter(data)
                list.adapter = adapter
            } else {
                adapter.updateData(data)
            }
        }
    }

    private lateinit var viewModel: EventsListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EventsListViewModel::class.java)
        binding.viewmodel = viewModel
        // TODO: Use the ViewModel
    }
}