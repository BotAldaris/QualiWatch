package com.example.qualiwatch.ui.screens.products

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.qualiwatch.R
import com.example.qualiwatch.ui.screens.products.utils.InputWithOCR
import com.example.qualiwatch.ui.screens.products.utils.MyDatePickerWithDialog
import com.example.qualiwatch.ui.screens.products.utils.SaveOfflineDialog
import com.example.qualiwatch.ui.screens.shared.SharedCameraxAddViewModel
import java.time.LocalDateTime

@Composable
fun AddEditProductScreen(
    viewmodel: AddEditProductViewModel,
    onProductUpdate: () -> Unit,
    onBack: () -> Unit,
    @StringRes title: Int,
    sharedCameraxAddViewModel: SharedCameraxAddViewModel,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = { AddEditProductTopAppBar(title, onBack) }
    ) { paddingValues ->
        val uiState by viewmodel.uiState.collectAsState()
        val sharedUiState by sharedCameraxAddViewModel.uiState.collectAsState()
        AddEditProductContent(
            name = uiState.name,
            batch = uiState.batch,
            expiration = uiState.expiration,
            onNameChange = viewmodel::updateName,
            onBatchChange = viewmodel::updateBatch,
            onExpirationChange = viewmodel::updateExpiration,
            onSave = viewmodel::saveProduct,
            showCamera = viewmodel::showCamera,
            isErrorBatch = uiState.hasErrorInBatch,
            isErrorName = uiState.hasErrorInName,
            nullErrorMessage = stringResource(R.string.error_empty),
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        )
        if (sharedUiState.imageProxy != null) {
            LaunchedEffect(viewmodel, sharedUiState, sharedCameraxAddViewModel) {
                viewmodel.onConfirmation(
                    sharedUiState.imageProxy!!,
                    sharedCameraxAddViewModel::updateImageProxy
                )
            }
        }
        if (uiState.showSaveOfflineDialog) {
            SaveOfflineDialog(
                onDismissRequest = { viewmodel.updateShowSaveOffline(false) },
                onConfirmation = viewmodel::onConfimationSaveOffline
            )


        }
        if (uiState.hasError) {
            val snackbarText = stringResource(R.string.saving_error)
            LaunchedEffect(snackbarHostState, snackbarText, viewmodel) {
                snackbarHostState.showSnackbar(snackbarText)
                viewmodel.updateHasError(false)
            }
        }
        LaunchedEffect(uiState.hasSaved) {
            if (uiState.hasSaved) {
                onProductUpdate()
            }
        }
    }
}

@Composable
private fun AddEditProductContent(
    name: String,
    batch: String,
    expiration: LocalDateTime,
    onNameChange: (String) -> Unit,
    onBatchChange: (String) -> Unit,
    onExpirationChange: (LocalDateTime?) -> Unit,
    isErrorName: Boolean,
    isErrorBatch: Boolean,
    onSave: () -> Unit,
    showCamera: (Int) -> Unit,
    nullErrorMessage: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        InputWithOCR(
            value = name,
            onValueChange = onNameChange,
            name = R.string.name,
            hasError = isErrorName,
            errorMessage = nullErrorMessage,
            showCamera = { showCamera(1) },
            modifier = Modifier.padding(top = 20.dp)
        )
        InputWithOCR(
            value = batch,
            onValueChange = onBatchChange,
            name = R.string.batch,
            hasError = isErrorBatch,
            errorMessage = nullErrorMessage,
            showCamera = { showCamera(2) },
            modifier = Modifier.padding(top = 20.dp)
        )
        Column(Modifier.padding(top = 20.dp)) {
            Text("* ${stringResource(R.string.expiration)}:")
            Row {
                MyDatePickerWithDialog(date = expiration, onConfirm = onExpirationChange)
                IconButton(onClick = { showCamera(3) }) {
                    Icon(imageVector = Icons.Default.PhotoCamera, contentDescription = null)
                }
            }
        }
        OutlinedButton(onClick = onSave) {
            Text(text = stringResource(R.string.save))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEditProductTopAppBar(@StringRes title: Int, onBack: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(title)) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(id = R.string.back))
            }
        }, modifier = Modifier.fillMaxWidth()
    )
}