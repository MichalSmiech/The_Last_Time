package com.michasoft.thelasttime.eventDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michasoft.thelasttime.model.Event
import com.michasoft.thelasttime.model.EventInstance
import com.michasoft.thelasttime.model.Label

/**
 * Created by mśmiech on 21.09.2023.
 */
@Composable
fun EventDetailsContent(
    event: Event,
    eventInstances: List<EventInstance>,
    onEventInstanceClick: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (event.labels.isNotEmpty()) {
            Labels(
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
                labels = event.labels
            )
        }
        EventInstanceList(eventInstances, onEventInstanceClick)
    }
}

@Composable
fun Labels(modifier: Modifier, labels: List<Label>) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        labels.forEach {
            LabelItem(label = it)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelItem(label: Label) {
    Surface(
        shape = InputChipDefaults.shape,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Box(
            modifier = Modifier.defaultMinSize(minHeight = InputChipDefaults.Height),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label.name,
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

val testLabels = listOf<Label>(
    Label("a", "test"),
    Label("a", "📖studia"),
)