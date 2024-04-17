package com.example.qualiwatch.ui.screens.products.utils


import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.qualiwatch.R


@Composable
fun SaveOfflineDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {

    AlertDialog(
        title = { Text(stringResource(id = R.string.connection_error_title)) },
        text = {
            Text(text = stringResource(id = R.string.save_offline))
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = {
                onConfirmation()
                onDismissRequest()
            }) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(text = stringResource(R.string.cancel))
            }
        })
}

@Composable
@Preview
fun SaveOfflineDialogPreview() {
    SaveOfflineDialog(
        onDismissRequest = { /*TODO*/ },
        onConfirmation = { /*TODO*/ },
    )
}