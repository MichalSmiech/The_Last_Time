package com.michasoft.thelasttime.eventdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstance

/**
 * Created by mśmiech on 21.09.2023.
 */
@Composable
fun EventDetailsContent(
    modifier: Modifier,
    event: Event,
    onEventNameChange: (String) -> Unit,
    onDiscardClick: () -> Unit,
    onDelete: () -> Unit,
    eventInstances: List<EventInstance>,
    onEventInstanceClick: (String) -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        TopBar(
            eventName = event.name,
            onEventNameChange = onEventNameChange,
            onDiscardClick = onDiscardClick,
            onDelete
        )
        EventInstanceList(eventInstances, onEventInstanceClick)
    }
}