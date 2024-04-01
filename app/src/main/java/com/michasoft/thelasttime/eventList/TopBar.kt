package com.michasoft.thelasttime.eventList

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.RenderVectorGroup
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.michasoft.thelasttime.model.Label
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Created by mśmiech on 28.09.2023.
 */
@Composable
fun TopBar(
    isErrorSync: Boolean,
    drawerState: DrawerState,
    userPhotoUrl: Uri?,
    labelFilter: Label?
) {
    val scope = rememberCoroutineScope()
    if (labelFilter != null) {
        LabelFilterTopBar(
            isErrorSync = isErrorSync,
            drawerState = drawerState,
            userPhotoUrl = userPhotoUrl,
            labelFilter = labelFilter,
            scope = scope
        )
    } else {
        DefaultTopBar(
            isErrorSync = isErrorSync,
            drawerState = drawerState,
            userPhotoUrl = userPhotoUrl,
            scope = scope
        )
    }
}

@Composable
private fun DefaultTopBar(
    isErrorSync: Boolean,
    drawerState: DrawerState,
    userPhotoUrl: Uri?,
    scope: CoroutineScope
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp),
        onClick = {},
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(48.dp)
        ) {
            MenuButton(onClick = {
                scope.launch {
                    drawerState.open()
                }
            })
            Text(
                text = "Search events",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )
            if (isErrorSync) {
                ErrorSyncButton({})
            }
            ProfileButton({}, userPhotoUrl)
        }
    }
}

@Composable
private fun LabelFilterTopBar(
    isErrorSync: Boolean,
    drawerState: DrawerState,
    userPhotoUrl: Uri?,
    labelFilter: Label,
    scope: CoroutineScope
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 4.dp)
            .height(48.dp)
    ) {
        MenuButton(onClick = {
            scope.launch {
                drawerState.open()
            }
        })
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = labelFilter.name,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )
        if (isErrorSync) {
            ErrorSyncButton({})
        }
        ProfileButton({}, userPhotoUrl)
    }
}

@Composable
fun MenuButton(
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 2.dp)
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
        modifier = Modifier.padding(start = 2.dp)
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
    userPhotoUrl: Uri?,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.padding(end = 2.dp)
    ) {
        if (userPhotoUrl != null) {
            AsyncImage(
                model = userPhotoUrl.toString(),
                contentDescription = "profile icon",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(30.dp),
                placeholder = rememberVectorPainter(
                    image = Icons.Default.Person,
                    LocalContentColor.current
                ),
                error = rememberVectorPainter(
                    image = Icons.Default.Person,
                    LocalContentColor.current
                ),
                fallback = rememberVectorPainter(
                    image = Icons.Default.Person,
                    LocalContentColor.current
                ),
            )
        } else {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "profile icon",
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewTopBar() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    TopBar(isErrorSync = true, drawerState = drawerState, userPhotoUrl = null, labelFilter = null)
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewTopBarLabelFilter() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    TopBar(isErrorSync = true, drawerState = drawerState, userPhotoUrl = null, labelFilter = Label("", "dom"))
}

@Composable
private fun rememberVectorPainter(image: ImageVector, tintColor: Color) =
    rememberVectorPainter(
        defaultWidth = image.defaultWidth,
        defaultHeight = image.defaultHeight,
        viewportWidth = image.viewportWidth,
        viewportHeight = image.viewportHeight,
        name = image.name,
        tintColor = tintColor,
        tintBlendMode = image.tintBlendMode,
        autoMirror = image.autoMirror,
        content = { _, _ -> RenderVectorGroup(group = image.root) }
    )