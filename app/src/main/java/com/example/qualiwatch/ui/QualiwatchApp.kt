package com.example.qualiwatch.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.qualiwatch.R
import com.example.qualiwatch.ui.screens.alerts.AlertScreen
import com.example.qualiwatch.ui.screens.alerts.AlertScreenViewModel
import com.example.qualiwatch.ui.screens.products.ProductScreen
import com.example.qualiwatch.ui.screens.settings.SettingsScreen
import com.example.qualiwatch.ui.screens.settings.SettingsViewModel


enum class QualiwatchAppEnum {
    Product,
    Config,
    Alert
}

@Composable
fun QualiwatchApp(navController: NavHostController = rememberNavController()) {
    var selectedScreen by remember {
        mutableStateOf(QualiwatchAppEnum.Product)
    }
    Scaffold(bottomBar = {
        QualiwatchAppBottomBar(navController = navController, selectedScreen = selectedScreen) {
            selectedScreen = it
        }
    }) {
        NavHost(
            navController = navController,
            startDestination = QualiwatchAppEnum.Product.name,
            modifier = Modifier.padding(it)
        ) {
            composable(QualiwatchAppEnum.Product.name) {
                ProductScreen()
            }
            composable(QualiwatchAppEnum.Config.name) {
                val viewModel = viewModel<SettingsViewModel>(factory = SettingsViewModel.Factory)
                SettingsScreen(viewModel)
            }
            composable(QualiwatchAppEnum.Alert.name) {
                val viewmodel =
                    viewModel<AlertScreenViewModel>(factory = AlertScreenViewModel.Factory)
                AlertScreen(viewModel = viewmodel)
            }
        }
    }

}

@Composable
fun QualiwatchAppBottomBar(
    navController: NavHostController,
    selectedScreen: QualiwatchAppEnum,
    setScreen: (QualiwatchAppEnum) -> Unit
) {
    NavigationBar {

        NavigationBarItem(
            selected = selectedScreen == QualiwatchAppEnum.Product,
            onClick = {
                navController.navigate(QualiwatchAppEnum.Product.name)
                setScreen(QualiwatchAppEnum.Product)
            },
            icon = { Icon(Icons.Filled.Home, contentDescription = null) },
            label = { Text(stringResource(R.string.products)) })
        NavigationBarItem(
            selected = selectedScreen == QualiwatchAppEnum.Config,
            onClick = {
                navController.navigate(QualiwatchAppEnum.Config.name)
                setScreen(QualiwatchAppEnum.Config)
            },
            icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
            label = { Text(stringResource(R.string.settings)) })
        NavigationBarItem(
            selected = selectedScreen == QualiwatchAppEnum.Alert,
            onClick = {
                navController.navigate(QualiwatchAppEnum.Alert.name)
                setScreen(QualiwatchAppEnum.Alert)
            },
            icon = { Icon(Icons.Filled.Notifications, contentDescription = null) },
            label = { Text(stringResource(R.string.alert)) })
    }
}