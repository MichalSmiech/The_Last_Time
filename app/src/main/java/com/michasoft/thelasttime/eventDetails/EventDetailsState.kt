package com.michasoft.thelasttime.eventDetails

import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstance

/**
 * Created by mśmiech on 21.09.2023.
 */
data class EventDetailsState(
    val isLoading: Boolean,
    val event: Event?,
    val eventInstances: List<EventInstance>,
    val isDeleteConfirmationDialogShowing: Boolean,
    val isBottomSheetShowing: Boolean
)