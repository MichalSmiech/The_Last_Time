package com.michasoft.thelasttime.eventDetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michasoft.thelasttime.model.EventInstance

/**
 * Created by mśmiech on 21.09.2023.
 */
@Composable
fun EventDetailsContent(
    eventInstances: List<EventInstance>,
    onEventInstanceClick: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        EventInstanceList(eventInstances, onEventInstanceClick)
    }
}