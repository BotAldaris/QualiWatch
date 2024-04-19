package com.example.qualiwatch.ui.screens.alerts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.qualiwatch.R
import com.example.qualiwatch.model.Product
import com.example.qualiwatch.ui.shared.ErrorScreen
import com.example.qualiwatch.ui.shared.LoadingScreen

@Composable
fun AlertScreen(
    viewModel: AlertScreenViewModel
) {
    val uiState by viewModel.alertScreenUiState.collectAsState()
    when (uiState.screen) {
        0 -> LoadingScreen()
        1 -> AlertPage(viewModel = viewModel, uiState = uiState)
        2 -> ErrorScreen(retryAction = viewModel::getAlerts)
    }
}

@Composable
fun AlertPage(viewModel: AlertScreenViewModel, uiState: AlertScreenUiState) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }, topBar = {
        AlertScreenTopAppBar(
            syncProduct = viewModel::syncProduct
        )
    }) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(it)
        ) {
            Row(modifier = Modifier.fillMaxWidth(0.95f)) {
                Text(stringResource(R.string.name), modifier = Modifier.weight(0.37f))
                Text(stringResource(R.string.batch), modifier = Modifier.weight(0.3f))
                Text(stringResource(R.string.date), modifier = Modifier.weight(0.33f))
            }
            HorizontalDivider()
            AlertList(
                products = uiState.products,
            )
        }
        uiState.userMessage?.let { message ->
            val snackbarText = stringResource(message)
            LaunchedEffect(snackbarHostState, viewModel, message, snackbarText) {
                snackbarHostState.showSnackbar(snackbarText)
                viewModel.snackbarMessageShown()
            }
        }
    }
}

@Composable
fun AlertList(
    products: List<Product>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        items(products, { item -> item.id }) {
            AlertCard(product = it, modifier.fillMaxWidth(0.95f))
            HorizontalDivider()
        }
    }
}

@Composable
fun AlertCard(product: Product, modifier: Modifier = Modifier) {
    Column(modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(product.nome, modifier = Modifier.weight(0.37f))
            Text(product.lote, modifier = Modifier.weight(0.3f))
            Text(
                product.validade.toString().split("T")[0].split("-").reversed().joinToString("/"),
                modifier = Modifier.weight(0.33f)
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlertScreenTopAppBar(
    syncProduct: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.alert)) },
        actions = {
            IconButton(
                onClick = syncProduct
            ) {
                Icon(painterResource(R.drawable.sync), stringResource(R.string.sync))
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}

