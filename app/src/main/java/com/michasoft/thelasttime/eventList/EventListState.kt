package com.michasoft.thelasttime.eventList

import android.net.Uri
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.Label

/**
 * Created by mśmiech on 25.09.2023.
 */
data class EventListState(
    val isLoading: Boolean,
    val events: List<Event>,
    val isErrorSync: Boolean,
    val isBottomSheetShowing: Boolean,
    val userPhotoUrl: Uri?,
    val labels: List<Label>,
    val labelFilter: Label?
)