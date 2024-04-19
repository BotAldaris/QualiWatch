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
        composable(
            route = "${ProductScreenEnum.Home.name}?message={message}",
            arguments = listOf(navArgument("message") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) {
            val message = it.arguments?.getInt("message")
            val homeProductsViewModel =
                viewModel<HomeProductsViewModel>(factory = HomeProductsViewModel.Factory)
            val homeProductsUiState by homeProductsViewModel.uiState.collectAsState()
            if (message != null) {
                if (message != -1) {
                    homeProductsViewModel.updateMessage(message)
                }
            }
            HomeScreen(
                homeProductsViewModel = homeProductsViewModel,
                homeProductsUiState = homeProductsUiState,
                retryAction = homeProductsViewModel::getProducts,
                editProductAction = { id ->
                    navController.navigate("${ProductScreenEnum.AddEdit.name}?id=$id")
                },
                deleteProductAction = homeProductsViewModel::deleteProduct,
                addProduct = { navController.navigate(ProductScreenEnum.AddEdit.name) },
            )
        }
        composable(
            route = "${ProductScreenEnum.AddEdit.name}?id={id}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            })
        ) {
            val id = it.arguments?.getString("id")
            val addEditProductViewModel = viewModel<AddEditProductViewModel>(
                factory = AddEditProductViewModelFactory(
                    QualiwatchApplication.appContainer.productsRepository,
                    QualiwatchApplication.appContainer.userPreferencesRepository,
                    { typeId -> navController.navigate("${ProductScreenEnum.Camerax.name}/$typeId") },
                    { messageID ->
                        navController.navigate("${ProductScreenEnum.Home.name}?message=$messageID") {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        }
                    },
                    id
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
                title = if (id == null) R.string.addProduct else R.string.editProduct
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