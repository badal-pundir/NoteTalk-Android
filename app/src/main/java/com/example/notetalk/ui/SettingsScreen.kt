package com.example.notetalk.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.notetalk.data.SettingsManager
import com.example.notetalk.data.Theme
import com.example.notetalk.ui.theme.NoteTalkTheme
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val currentTheme by viewModel.theme.collectAsStateWithLifecycle(initialValue = Theme.SYSTEM)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings")},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack()}) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ){
            Text("Theme", style =  MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            ThemeOption(
                text = "Light",
                isSelected = currentTheme == Theme.LIGHT,
                onClick = { viewModel.setTheme(Theme.LIGHT)}
            )
            ThemeOption(
                text = "Dark",
                isSelected = currentTheme == Theme.DARK,
                onClick = { viewModel.setTheme(Theme.DARK)}
            )
            ThemeOption(
                text = "System Default",
                isSelected = currentTheme == Theme.SYSTEM,
                onClick = { viewModel.setTheme(Theme.SYSTEM)}
        )
        }

    }
}

@Composable
fun ThemeOption(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick
        )
//        Spacer(Modifier.width(8.dp))
        Text(text = text)
    }
}


@Preview(name = "Theme option")
@Composable
fun ThemeOptionPreview() {
    NoteTalkTheme {
        ThemeOption(text = "Light", isSelected = true, onClick = {})
    }
}
