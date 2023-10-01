package com.michasoft.thelasttime.eventList

import com.michasoft.thelasttime.model.Event

/**
 * Created by mśmiech on 25.09.2023.
 */
data class EventListState(
    val isLoading: Boolean,
    val events: List<Event>,
    val isErrorSync: Boolean,
    val isBottomSheetShowing: Boolean
)