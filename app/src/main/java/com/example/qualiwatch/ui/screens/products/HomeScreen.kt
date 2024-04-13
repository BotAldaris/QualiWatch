package com.example.qualiwatch.ui.screens.products

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.qualiwatch.R
import com.example.qualiwatch.model.Product
import com.example.qualiwatch.ui.screens.products.utils.DateUtils
import com.example.qualiwatch.ui.shared.ErrorScreen
import com.example.qualiwatch.ui.shared.LoadingScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeProductsUiState: HomeProductsUiState,
    homeProductsViewModel: HomeProductsViewModel,
    editProductAction: (String) -> Unit,
    deleteProductAction: (Product) -> Unit,
    updateHasError: (Boolean) -> Unit,
    retryAction: () -> Unit,
    addProduct: () -> Unit,
    modifier: Modifier = Modifier,
    dateUtils: DateUtils = DateUtils(),
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            HomeProductsTopAppBar(
                addProduct = addProduct,
                syncProduct = homeProductsViewModel::syncProduct
            )
        }) {
        when (homeProductsUiState.screen) {
            0 -> LoadingScreen(
                modifier
                    .padding(it)
                    .fillMaxWidth()
            )

            1 -> {
                Column(
                    modifier = modifier
                        .padding(it)
                        .fillMaxWidth()
                ) {
                    SearchBar(
                        query = homeProductsUiState.query,
                        onQueryChange = homeProductsViewModel::updateQuery,
                        onSearch = homeProductsViewModel::search,
                        active = false,
                        onActiveChange = {},
                        leadingIcon = { Icon(Icons.Default.Search, null) },
                        placeholder = { Text(stringResource(R.string.search)) },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                    }
                    ProductGridScreen(
                        homeProductsUiState.filteredProducts,
                        editProductAction = editProductAction,
                        deleteProductAction = deleteProductAction,
                        dateUtils = dateUtils
                    )
                }

            }

            2 -> ErrorScreen(
                retryAction,
                modifier
                    .padding(it)
                    .fillMaxWidth()
            )
        }
        if (homeProductsUiState.hasError) {
            val snackbarText = stringResource(R.string.delete_error)
            LaunchedEffect(snackbarHostState, snackbarText, updateHasError) {
                snackbarHostState.showSnackbar(snackbarText)
                updateHasError(false)
            }
        }
    }

}

@Composable
private fun ProductGridScreen(
    products: List<Product>,
    editProductAction: (String) -> Unit,
    deleteProductAction: (Product) -> Unit,
    modifier: Modifier = Modifier,
    dateUtils: DateUtils,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(300.dp),
        modifier = modifier.padding(horizontal = 8.dp),
    ) {
        items(products, { product -> product.id }) { product ->
            ProductCard(
                product,
                editProductAction,
                deleteProductAction,
                dateUtils,
                Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
private fun ProductCard(
    product: Product,
    editProductAction: (String) -> Unit,
    deleteProductAction: (Product) -> Unit,
    dateUtils: DateUtils,
    modifier: Modifier = Modifier
) {
    Card(modifier) {
        Column(Modifier.padding(8.dp)) {
            Text(product.nome, style = MaterialTheme.typography.titleMedium)
            Text("${stringResource(R.string.batch)}: ${product.lote}")
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("${stringResource(R.string.expiration)}: ${dateUtils.dateToString(product.validade.toLocalDate())}")
                ExpirationIcon(
                    expiration = product.validade,
                    Modifier.padding(end = 12.dp)
                )
            }
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                OutlinedIconButton(onClick = { editProductAction(Json.encodeToString(product)) }) {
                    Icon(painterResource(R.drawable.edit), stringResource(R.string.edit))
                }
                FilledTonalIconButton(onClick = { deleteProductAction(product) }) {
                    Icon(painterResource(R.drawable.delete), stringResource(R.string.delete))
                }
            }
        }
    }
}

@Composable
private fun ExpirationIcon(expiration: LocalDateTime, modifier: Modifier = Modifier) {
    val timeRemaining = ChronoUnit.DAYS.between(LocalDate.now(), expiration)
    if (timeRemaining <= 0) {
        Image(
            painter = painterResource(R.drawable.ic_warning),
            contentDescription = stringResource(R.string.warning),
            modifier = modifier
        )
    } else if (timeRemaining <= 30) {
        Image(
            painter = painterResource(R.drawable.clock_alert),
            contentDescription = stringResource(R.string.close),
            modifier = modifier
        )
    } else {
        Image(
            painter = painterResource(R.drawable.clock),
            contentDescription = stringResource(R.string.normal),
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeProductsTopAppBar(
    addProduct: () -> Unit,
    syncProduct: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.products)) },
        actions = {
            IconButton(
                onClick = addProduct
            ) {
                Icon(Icons.Filled.Add, stringResource(R.string.addProduct))
            }
            IconButton(
                onClick = syncProduct
            ) {
                Icon(painterResource(R.drawable.sync), stringResource(R.string.sync))
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}


@Preview
@Composable
private fun ProductCardPreview() {
    val product = Product("1mdo3d", "Mamão", "1d9xa", LocalDateTime.now())
    ProductCard(product = product, {}, {}, DateUtils())
}

@Preview
@Composable
private fun HomeProductsTopAppBarPreview() {
    HomeProductsTopAppBar({}, {})
}
