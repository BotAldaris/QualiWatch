package com.example.qualiwatch.ui.screens.alerts

import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.qualiwatch.R
import com.example.qualiwatch.model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertScreen(
    viewModel: AlertScreenViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.alertScreenUiState.collectAsState()
    val pullRefreshState =
        rememberPullToRefreshState(PullToRefreshDefaults.PositionalThreshold)
    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }, topBar = {
        AlertScreenTopAppBar(
            deleteAllProducts = viewModel::deleteAll,
            syncProduct = viewModel::syncProduct
        )
    }) {
        Box(
            Modifier
                .padding(it)
                .nestedScroll(pullRefreshState.nestedScrollConnection)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(modifier = Modifier.fillMaxWidth(0.95f)) {
                    Text(stringResource(R.string.name), modifier = Modifier.weight(0.4f))
                    Text(stringResource(R.string.batch), modifier = Modifier.weight(0.4f))
                    Text(stringResource(R.string.delete), modifier = Modifier.weight(0.2f))
                }
                HorizontalDivider()
                AlertList(
                    products = uiState.products,
                    deleteProduct = viewModel::deleteProduct
                )

            }
            PullToRefreshContainer(
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
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
    deleteProduct: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        items(products, { item -> item.id }) {
            AlertCard(product = it, deleteProduct = deleteProduct, modifier.fillMaxWidth(0.95f))
            HorizontalDivider()
        }
    }
}

@Composable
fun AlertCard(product: Product, deleteProduct: (Product) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(product.nome, modifier = Modifier.weight(0.4f))
            Text(product.lote, modifier = Modifier.weight(0.4f))
            Row(modifier = Modifier.weight(0.2f)) {
                IconButton(onClick = { deleteProduct(product) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.delete),
                        contentDescription = stringResource(R.string.delete)
                    )
                }
            }

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlertScreenTopAppBar(
    deleteAllProducts: () -> Unit,
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
            IconButton(
                onClick = deleteAllProducts
            ) {
                Icon(painterResource(R.drawable.delete_empty), stringResource(R.string.deleteAll))
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}

