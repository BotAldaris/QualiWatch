package com.example.qualiwatch.ui.screens.products

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.qualiwatch.QualiwatchApplication
import com.example.qualiwatch.R
import com.example.qualiwatch.ui.screens.camerax.CameraxScreen
import com.example.qualiwatch.ui.screens.camerax.CameraxViewModel
import com.example.qualiwatch.ui.screens.camerax.CameraxViewModelFactory
import com.example.qualiwatch.ui.screens.shared.SharedCameraxAddViewModel


enum class ProductScreenEnum {
    Home,
    AddEdit,
    Camerax
}

@Composable
fun ProductScreen(navController: NavHostController = rememberNavController()) {
    val sharedCameraxAddViewModel = viewModel<SharedCameraxAddViewModel>()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = ProductScreenEnum.Home.name) {
        composable(route = ProductScreenEnum.Home.name) {
            val homeProductsViewModel =
                viewModel<HomeProductsViewModel>(factory = HomeProductsViewModel.Factory)
            val homeProductsUiState by homeProductsViewModel.uiState.collectAsState()
            HomeScreen(
                homeProductsViewModel = homeProductsViewModel,
                homeProductsUiState = homeProductsUiState,
                retryAction = homeProductsViewModel::getProducts,
                editProductAction = {
                    navController.navigate("${ProductScreenEnum.AddEdit.name}?product=$it")
                },
                deleteProductAction = homeProductsViewModel::deleteProduct,
                updateHasError = homeProductsViewModel::updateError,
                addProduct = { navController.navigate(ProductScreenEnum.AddEdit.name) },
            )
        }
        composable(
            route = "${ProductScreenEnum.AddEdit.name}?product={product}",
            arguments = listOf(navArgument("product") {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            })
        ) {
            val product = it.arguments?.getString("product")
            val addEditProductViewModel = viewModel<AddEditProductViewModel>(
                factory = AddEditProductViewModelFactory(
                    QualiwatchApplication.appContainer.productsRepository,
                    { typeId -> navController.navigate("${ProductScreenEnum.Camerax.name}/$typeId") },
                    product
                )
            )
            AddEditProductScreen(
                viewmodel = addEditProductViewModel,
                sharedCameraxAddViewModel = sharedCameraxAddViewModel,
                onProductUpdate = {
                    navController.navigate(ProductScreenEnum.Home.name) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                },
                onBack = { navController.popBackStack() },
                title = if (product == null) R.string.addProduct else R.string.editProduct
            )
        }
        composable(
            route = "${ProductScreenEnum.Camerax.name}/{type}",
            arguments = listOf(navArgument("type") {
                type = NavType.IntType
                defaultValue = 1
            })
        ) { entry ->
            val type = entry.arguments?.getInt("type") ?: 1
            Log.d("type", type.toString())
            val cameraxViewModel = viewModel<CameraxViewModel>(
                factory = CameraxViewModelFactory(
                    QualiwatchApplication.appContainer::imageToText,
                    type
                )
            )
            CameraxScreen(
                context = context,
                viewmodel = cameraxViewModel
            ) {
                sharedCameraxAddViewModel.updateImageProxy(it)
                navController.navigateUp()
            }
        }
    }
}