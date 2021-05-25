package com.michasoft.thelasttime.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.michasoft.thelasttime.model.EventType
import com.michasoft.thelasttime.model.repo.IEventsRepository
import javax.inject.Inject

/**
 * Created by mśmiech on 02.05.2021.
 */
class EventTypeListViewModel @Inject constructor(
    private val eventsRepository: IEventsRepository
): ViewModel() {
    var eventTypes = MutableLiveData<List<EventType>>(eventsRepository.getEventTypes())

}