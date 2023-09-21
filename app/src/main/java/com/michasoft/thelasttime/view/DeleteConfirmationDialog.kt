package com.michasoft.thelasttime.view

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.michasoft.thelasttime.R

/**
 * Created by mśmiech on 22.09.2023.
 */

@Composable
fun DeleteConfirmationDialog(
    onDismissDialog: () -> Unit,
    onConfirmClicked: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissDialog,
        title = { Text(stringResource(R.string.delete_confirmation_title)) },
        confirmButton = {
            TextButton(onClick = onConfirmClicked) {
                Text(stringResource(R.string.action_ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissDialog) {
                Text(stringResource(R.string.action_cancel))
            }
        },
    )
}