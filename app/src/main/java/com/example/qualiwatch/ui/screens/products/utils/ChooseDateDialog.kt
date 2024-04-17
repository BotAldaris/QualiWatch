package com.example.qualiwatch.ui.screens.products.utils


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.qualiwatch.R
import com.example.qualiwatch.model.ImageResponse


@Composable
fun ChooseDateDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: (String?) -> Unit,
    imageResponses: List<ImageResponse>
) {
    var idSelected by remember { mutableIntStateOf(1) }
    var imageResponse: ImageResponse? by remember {
        mutableStateOf(if (imageResponses.isNotEmpty()) imageResponses[0] else null)
    }

    AlertDialog(
        title = { Text(stringResource(id = R.string.date_input)) },
        text = {
            LazyColumn {
                items(imageResponses, { item -> item.id }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = it.id == idSelected,
                            onClick = {
                                idSelected = it.id
                                imageResponse = it
                            })
                        Text(text = it.data)
                    }

                }
            }
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = {
                onConfirmation(imageResponse?.data)
                onDismissRequest()
            }) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(stringResource(id = R.string.dismiss))
            }
        })
}

@Composable
@Preview
fun ChooseDateDialogPreview() {
    val l =
        listOf(ImageResponse(1, "a"), ImageResponse(2, "b"), ImageResponse(3, "c"))
    ChooseDateDialog(
        onDismissRequest = { /*TODO*/ },
        onConfirmation = { /*TODO*/ },
        imageResponses = l
    )
}