package com.michasoft.thelasttime.eventAdd

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.michasoft.thelasttime.view.NoShapeTextField

/**
 * Created by mśmiech on 28.09.2023.
 */
@Composable
fun EventAddContent(
    eventName: String,
    onEventNameChange: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    Column(
        modifier = Modifier.padding(16.dp, 8.dp)
    ) {
        NoShapeTextField(
            modifier = Modifier.focusRequester(focusRequester),
            singleLine = true,
            value = eventName,
            onValueChange = onEventNameChange,
            textStyle = LocalTextStyle.current.copy(fontSize = 20.sp),
            placeholder = { Text(text = "Enter event name", fontSize = 20.sp) }
        )
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}