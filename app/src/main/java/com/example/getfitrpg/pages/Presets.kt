package com.example.getfitrpg.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.getfitrpg.ui.theme.GetFitRPGTheme

// Colors from the design
private val DarkGreen = Color(0xFF3A4B3F)
private val LightGreen = Color(0xFF8BC34A)
private val BackgroundDark = Color(0xFF232528)
private val AlmostWhite = Color(0xFFE0E0E0)
private val CardBackground = Color(0xFFFAFAFA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PresetsScreen(modifier: Modifier = Modifier) {
    var isFabMenuVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Presets", color = AlmostWhite) },
                navigationIcon = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = AlmostWhite)
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = AlmostWhite)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundDark)
            )
        },
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                AnimatedVisibility(visible = isFabMenuVisible) {
                    Column(horizontalAlignment = Alignment.End) {
                        FloatingActionButton(
                            onClick = { /* TODO */ },
                            modifier = Modifier.padding(bottom = 8.dp),
                            containerColor = DarkGreen,
                            contentColor = AlmostWhite
                        ) {
                            Row(Modifier.padding(horizontal = 16.dp)) {
                                Icon(Icons.Default.Edit, contentDescription = "Create from Template")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Create From Template")
                            }
                        }
                        FloatingActionButton(
                            onClick = { /* TODO */ },
                            modifier = Modifier.padding(bottom = 16.dp),
                            containerColor = DarkGreen,
                            contentColor = AlmostWhite
                        ) {
                             Row(Modifier.padding(horizontal = 16.dp)) {
                                Icon(Icons.Default.Add, contentDescription = "Create Empty Preset")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Create Empty Preset")
                            }
                        }
                    }
                }
                FloatingActionButton(
                    onClick = { isFabMenuVisible = !isFabMenuVisible },
                     containerColor = LightGreen,
                ) {
                    Icon(
                        if (isFabMenuVisible) Icons.Default.Close else Icons.Default.Add,
                        contentDescription = "Toggle FAB Menu"
                    )
                }
            }

        },
        containerColor = BackgroundDark
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items((1..7).toList()) { _ ->
                PresetItem()
            }
        }
    }
}

@Composable
fun PresetItem() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(LightGreen.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Text("A", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Header", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Subhead", color = Color.Gray, fontSize = 14.sp)
            }
            // Icons on the right would go here
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF232528)
@Composable
fun PresetsScreenPreview() {
    GetFitRPGTheme {
        PresetsScreen()
    }
}
