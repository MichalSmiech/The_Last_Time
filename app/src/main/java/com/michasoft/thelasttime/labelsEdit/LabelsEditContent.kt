package com.michasoft.thelasttime.labelsEdit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import com.michasoft.thelasttime.model.Label
import com.michasoft.thelasttime.view.NoShapeTextField

/**
 * Created by mśmiech on 27.11.2023.
 */
@Composable
fun LabelsEditContent(
    state: LabelsEditState,
    onLabelNameChange: (Label, String) -> Unit,
    newLabelFocusRequester: FocusRequester,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LabelItemList(
            labelItems = state.labels,
            onLabelNameChange = onLabelNameChange,
            newLabelFocusRequester = newLabelFocusRequester
        )
    }
}

@Composable
fun LabelItemList(
    labelItems: List<Label>,
    onLabelNameChange: (Label, String) -> Unit,
    newLabelFocusRequester: FocusRequester,
) {
    LazyColumn(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxSize(),
    ) {
        item {
            NewLabelItemUI(newLabelFocusRequester)
        }
        items(items = labelItems, key = { it.id }) {
            LabelItemUI(
                label = it,
                onLabelNameChange = onLabelNameChange
            )
        }
    }
}

@Composable
fun NewLabelItemUI(
    focusRequester: FocusRequester
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(start = 8.dp)
                .size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "add label icon"
            )
        }
        NoShapeTextField(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
                .focusRequester(focusRequester),
            singleLine = true,
            value = "",
            onValueChange = { },
            placeholder = { Text(text = "Create new label") }
        )
    }
}

@Composable
fun LabelItemUI(
    label: Label,
    onLabelNameChange: (Label, String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(start = 8.dp)
                .size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Label,
                contentDescription = "label icon"
            )
        }
        NoShapeTextField(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            singleLine = true,
            value = label.name,
            onValueChange = { onLabelNameChange(label, it) }
        )
        Box(
            modifier = Modifier
                .padding(end = 8.dp)
                .size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "label icon"
            )
        }
    }
}