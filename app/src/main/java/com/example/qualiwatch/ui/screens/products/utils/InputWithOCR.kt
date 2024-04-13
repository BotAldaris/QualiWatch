package com.example.qualiwatch.ui.screens.products.utils

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction

@Composable
fun InputWithOCR(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes name: Int,
    hasError: Boolean,
    errorMessage: String,
    showCamera: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(text = "*${stringResource(name)}:")
        Row {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text(text = stringResource(name)) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .semantics { if (hasError) error(errorMessage) },
                supportingText = { if (hasError) Text(errorMessage) },
                isError = hasError
            )
            IconButton(onClick = { showCamera(2) }) {
                Icon(imageVector = Icons.Default.PhotoCamera, contentDescription = null)
            }
        }

    }
}