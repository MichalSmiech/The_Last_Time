package com.michasoft.thelasttime.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.michasoft.thelasttime.model.EventType

/**
 * Created by mśmiech on 02.05.2021.
 */
class EventTypeListViewModel: ViewModel() {
    var eventTypes = MutableLiveData<List<EventType>>()

}