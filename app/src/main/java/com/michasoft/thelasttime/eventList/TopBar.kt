package com.michasoft.thelasttime.eventList

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Created by mśmiech on 28.09.2023.
 */
@Composable
fun TopBar(
    isErrorSync: Boolean,
    drawerState: DrawerState
) {
    val scope = rememberCoroutineScope()
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 8.dp)
            .clickable { },
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            MenuButton(onClick = {
                scope.launch {
                    drawerState.open()
                }
            })
            Text(text = "Search events", modifier = Modifier.weight(1f))
            if (isErrorSync) {
                ErrorSyncButton({})
            }
            ProfileButton({})
        }
    }
}

@Composable
fun MenuButton(
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.padding(8.dp, 2.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "menu icon"
        )
    }
}

@Composable
fun ErrorSyncButton(
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.padding(top = 2.dp, bottom = 2.dp, start = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = "error sync icon",
            tint = MaterialTheme.colorScheme.error,
        )
    }
}

@Composable
fun ProfileButton(
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.padding(top = 2.dp, bottom = 2.dp, end = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "profile icon"
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewTopBar() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    TopBar(isErrorSync = true, drawerState = drawerState)
}