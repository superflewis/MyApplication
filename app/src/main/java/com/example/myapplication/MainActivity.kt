package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RectangleShape // Explicitly ensure this is here
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.network.RetrofitInstance
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RomDownloaderApp() {
    val roms = remember { mutableStateListOf<Rom>() }
    val selectedRoms = remember { mutableStateListOf<Rom>() }
    val coroutineScope = rememberCoroutineScope()
    val systems = listOf("NS", "GC", "PS2", "WII", "PSP")
    var selectedSystem by remember { mutableStateOf("NS") }

    // Fetch ROMs (mock data for now)
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val fetchedRoms = RetrofitInstance.api.getRoms()
                roms.clear()
                roms.addAll(fetchedRoms)
            } catch (e: Exception) {
                Log.e("RomDownloader", "Error fetching ROMs: $e")
            }
        }
    }

    // Filter ROMs by selected system
    val filteredRoms = remember(selectedSystem) {
        roms.filter { it.title.contains(selectedSystem, ignoreCase = true) }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("ROM Downloader") },
                actions = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        systems.forEach { system ->
                            OutlinedButton(
                                onClick = { selectedSystem = system },
                                modifier = Modifier
                                    .border(2.dp, MaterialTheme.colorScheme.outline)
                                    .height(32.dp),
                                shape = RectangleShape, // Should resolve now
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (selectedSystem == system) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Text(
                                    text = system,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                Log.d("RomDownloader", "Selected ROMs: $selectedRoms")
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
            items(filteredRoms) { rom ->
                RomItem(
                    title = rom.title,
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
fun RomItem(title: String, isSelected: Boolean, onCheckedChange: (Boolean