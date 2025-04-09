package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                RomDownloaderApp()
            }
        }
    }
}

@Composable
fun RomDownloaderApp() {
    // Placeholder ROM list (replace with real data later)
    val roms = remember { mutableStateListOf("Super Mario", "Zelda", "Metroid") }
    val selectedRoms = remember { mutableStateListOf<String>() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("ROM Downloader") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                println("Selected ROMs: $selectedRoms") // TODO: Start downloads
            }) {
                Text("Download (${selectedRoms.size})")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(roms) { rom ->
                RomItem(
                    title = rom,
                    isSelected = rom in selectedRoms,
                    onCheckedChange = { isChecked ->
                        if (isChecked) selectedRoms.add(rom)
                        else selectedRoms.remove(rom)
                    }
                )
            }
        }
    }
}

@Composable
fun RomItem(title: String, isSelected: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Checkbox(
                checked = isSelected,
                onCheckedChange = onCheckedChange
            )
        }
    }
}