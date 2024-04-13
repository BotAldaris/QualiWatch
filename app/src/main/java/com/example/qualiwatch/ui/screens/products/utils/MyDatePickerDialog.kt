package com.example.qualiwatch.ui.screens.products.utils

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.qualiwatch.R
import java.time.LocalDateTime
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePickerWithDialog(
    modifier: Modifier = Modifier,
    date: LocalDateTime,
    onConfirm: (LocalDateTime?) -> Unit
) {
    val dateState = rememberDatePickerState(
        initialSelectedDateMillis = date.toInstant(ZoneOffset.UTC)
            .toEpochMilli()
    )
    val millisToLocalDate = dateState.selectedDateMillis?.let {
        DateUtils().convertMillisToLocalDate(it)
    }
    var showDialog by remember { mutableStateOf(false) }
    Column(modifier)
    {
        OutlinedButton(
            onClick = { showDialog = true },
            shape = OutlinedTextFieldDefaults.shape,
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = DateUtils().dateToString(date.toLocalDate()),
                textAlign = TextAlign.Center,
            )
        }


        if (showDialog) {
            DatePickerDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog = false
                            onConfirm(millisToLocalDate?.atStartOfDay())
                        }
                    ) {
                        Text(text = "OK")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDialog = false }
                    ) {
                        Text(text = stringResource(R.string.cancel))
                    }
                }
            ) {
                DatePicker(
                    state = dateState,
                    showModeToggle = true
                )
            }
        }
    }
}