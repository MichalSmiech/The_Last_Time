package com.michasoft.thelasttime.eventlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michasoft.thelasttime.model.Event

/**
 * Created by mśmiech on 25.09.2023.
 */
@Composable
fun EventListContent(
    events: List<Event>,
    onEventClick: (String) -> Unit,
    onInstanceAdd: (String) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        EventList(events, onEventClick, onInstanceAdd)
    }
}