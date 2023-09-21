package com.michasoft.thelasttime.eventinstancedetails

import com.michasoft.thelasttime.model.EventInstance

/**
 * Created by mśmiech on 21.09.2023.
 */
data class EventInstanceDetailsState(
    val isLoading: Boolean,
    val eventName: String,
    val eventInstance: EventInstance?,
    val isDeleteConfirmationDialogShowing: Boolean
)