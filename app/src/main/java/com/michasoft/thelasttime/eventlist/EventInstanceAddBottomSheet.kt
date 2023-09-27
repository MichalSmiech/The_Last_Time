package com.michasoft.thelasttime.eventlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.michasoft.thelasttime.view.EditableDate
import com.michasoft.thelasttime.view.EditableTime

/**
 * Created by mśmiech on 25.09.2023.
 */
@Composable
fun EventInstanceAddBottomSheet(viewModel: EventInstanceAddViewModel) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    if (state.instance == null) {
        return
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            EditableDate(
                date = state.instance.timestamp.toLocalDate(),
                onDateChange = viewModel::changeDate
            )
            EditableTime(
                time = state.instance.timestamp.toLocalTime(),
                onTimeChange = viewModel::changeTime
            )
        }
        TextButton(
            modifier = Modifier.padding(8.dp),
            onClick = viewModel::onSaveButtonClicked
        ) {
            Text(text = "ADD")
        }
    }
}