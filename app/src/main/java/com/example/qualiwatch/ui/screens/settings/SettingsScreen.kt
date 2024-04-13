package com.example.qualiwatch.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.qualiwatch.R

@Composable
fun SettingsScreen(viewModel: SettingsViewModel, modifier: Modifier = Modifier) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(topBar = { SettingsScreenTopAppBar() }) {
        Column(
            modifier = modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("${stringResource(R.string.theme)}:${stringResource(if (uiState.userPreferences.dark) R.string.dark else R.string.light)}")
            Switch(checked = uiState.userPreferences.dark, onCheckedChange = viewModel::updateDark)
            Text("${stringResource(R.string.save)}:${stringResource(if (uiState.userPreferences.online) R.string.online else R.string.offline)}")
            Switch(
                checked = uiState.userPreferences.online,
                onCheckedChange = viewModel::updateSaveOnline
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenTopAppBar(modifier: Modifier = Modifier) {
    TopAppBar(title = { Text(stringResource(R.string.settings)) }, modifier = modifier)
}