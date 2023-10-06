package com.michasoft.thelasttime.eventLabels

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.michasoft.thelasttime.eventLabels.model.LabelItem
import com.michasoft.thelasttime.model.Label

/**
 * Created by mśmiech on 06.10.2023.
 */
@Composable
fun EventLabelsContent(
    labelItems: List<LabelItem>,
    onLabelItemCheckedChange: (Label, Boolean) -> Unit,
    newLabelName: String,
    onNewLabelAdd: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LabelItemList(labelItems, onLabelItemCheckedChange, newLabelName, onNewLabelAdd)
    }
}

@Composable
fun LabelItemList(
    labelItems: List<LabelItem>,
    onLabelItemCheckedChange: (Label, Boolean) -> Unit,
    newLabelName: String,
    onNewLabelAdd: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxSize(),
    ) {
        if (newLabelName.isNotBlank()) {
            item(newLabelName) {
                NewLabelItemUI(name = newLabelName, onNewLabelAdd = onNewLabelAdd)
            }
        }
        items(labelItems) {
            LabelItemUI(it, onLabelItemCheckedChange)
        }
    }
}

@Composable
fun LabelItemUI(labelItem: LabelItem, onCheckedChange: (Label, Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .clickable { onCheckedChange(labelItem.label, !labelItem.checked) }
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
        Text(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .weight(1f), text = labelItem.label.name
        )
        Checkbox(
            modifier = Modifier.padding(end = 8.dp),
            checked = labelItem.checked,
            onCheckedChange = { checked -> onCheckedChange(labelItem.label, checked) })
    }
}

@Composable
fun NewLabelItemUI(name: String, onNewLabelAdd: (String) -> Unit) {
    Row(
        modifier = Modifier
            .clickable { onNewLabelAdd(name) }
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
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "add label icon"
            )
        }
        Text(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .weight(1f),
            text = "Utwórz \"$name\""
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLabelItemUI() {
    LabelItemUI(
        labelItem = LabelItem(Label("1", "test"), checked = true),
        onCheckedChange = { label: Label, b: Boolean -> })
}