package com.michasoft.thelasttime.labelsEdit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
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
    onNewLabelNameChange: (String) -> Unit,
    onNewLabelAdd: (String) -> Unit,
    onLabelDelete: (Label) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LabelItemList(
            labelItems = state.labels,
            onLabelNameChange = onLabelNameChange,
            newLabelFocusRequester = newLabelFocusRequester,
            newLabelName = state.newLabelName,
            onNewLabelNameChange = onNewLabelNameChange,
            onNewLabelAdd = onNewLabelAdd,
            onLabelDelete = onLabelDelete,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LabelItemList(
    labelItems: List<Label>,
    onLabelNameChange: (Label, String) -> Unit,
    newLabelFocusRequester: FocusRequester,
    newLabelName: String,
    onNewLabelNameChange: (String) -> Unit,
    onNewLabelAdd: (String) -> Unit,
    onLabelDelete: (Label) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxSize(),
    ) {
        item {
            NewLabelItemUI(
                name = newLabelName,
                focusRequester = newLabelFocusRequester,
                onNameChange = onNewLabelNameChange,
                onNewLabelAdd = onNewLabelAdd,
            )
        }
        items(items = labelItems, key = { it.id }) {
            LabelItemUI(
                modifier = Modifier.animateItemPlacement(),
                label = it,
                onLabelNameChange = onLabelNameChange,
                onLabelDelete = onLabelDelete
            )
        }
    }
}

@Composable
fun NewLabelItemUI(
    name: String,
    focusRequester: FocusRequester,
    onNameChange: (String) -> Unit,
    onNewLabelAdd: (String) -> Unit,
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    Column {
        if (isFocused) {
            Divider()
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isFocused) {
                LeadingIcon(icon = Icons.Default.Clear, onClick = {
                    focusManager.clearFocus()
                    onNameChange("")
                })
            } else {
                LeadingIcon(icon = Icons.Default.Add)
            }
            NoShapeTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .onFocusChanged {
                        isFocused = it.isFocused
                    }
                    .focusRequester(focusRequester),
                singleLine = true,
                value = name,
                onValueChange = onNameChange,
                placeholder = { Text(text = "Create new label") },
                keyboardActions = KeyboardActions(onDone = {
                    if (name.isNotBlank()) {
                        focusManager.clearFocus()
                        onNewLabelAdd(name)
                    }
                }),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
            if (isFocused) {
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(48.dp)
                        .clickable {
                            if (name.isNotBlank()) {
                                focusManager.clearFocus()
                                onNewLabelAdd(name)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "add label icon"
                    )
                }
            }
        }
        if (isFocused) {
            Divider()
        }
    }
}

@Composable
fun LabelItemUI(
    modifier: Modifier,
    label: Label,
    onLabelNameChange: (Label, String) -> Unit,
    onLabelDelete: (Label) -> Unit,
) {
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }
    Column {
        if (isFocused) {
            Divider()
        }
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(56.dp)
                .onFocusChanged {
                    isFocused = it.isFocused
                }
                .focusRequester(focusRequester),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isFocused) {
                LeadingIcon(icon = Icons.Outlined.Delete, onClick = {
                    focusManager.clearFocus()
                    onLabelDelete(label)
                })
            } else {
                LeadingIcon(icon = Icons.Outlined.Label)
            }
            NoShapeTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                singleLine = true,
                value = label.name,
                onValueChange = { onLabelNameChange(label, it) },
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
            if (isFocused) {
                TrailingIcon(icon = Icons.Outlined.Check, onClick = {
                    focusManager.clearFocus()
                })
            } else {
                TrailingIcon(icon = Icons.Outlined.Edit, onClick = {
                    focusRequester.requestFocus()
                })
            }
        }
        if (isFocused) {
            Divider()
        }
    }
}


@Composable
private fun LeadingIcon(
    icon: ImageVector,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .padding(start = 8.dp)
            .size(48.dp)
            .let { if (onClick != null) it.clickable { onClick() } else it },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )
    }
}

@Composable
private fun TrailingIcon(
    icon: ImageVector,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .padding(end = 8.dp)
            .size(48.dp)
            .let { if (onClick != null) it.clickable { onClick() } else it },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )
    }
}