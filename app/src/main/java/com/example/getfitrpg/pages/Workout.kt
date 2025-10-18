package com.example.getfitrpg.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
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
private val CardBackground = Color(0xFF303236)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutsScreen(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Workouts", color = AlmostWhite) },
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
        containerColor = BackgroundDark
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
        ) {
            // Search Bar
            var searchText by remember { mutableStateOf("") }
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Hinted search text") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = CardBackground,
                    unfocusedContainerColor = CardBackground,
                    disabledContainerColor = CardBackground,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            // Filter Chips
            val filters = listOf("Legs", "Label", "Label", "Label", "Label")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filters) { filter ->
                    SuggestionChip(
                        onClick = { /* TODO */ },
                        label = { Text(filter) },
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Workout List
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(6) { _ ->
                    WorkoutListItem()
                }
            }
        }
    }
}

@Composable
fun WorkoutListItem() {
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
                Text("Header", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = AlmostWhite)
                Text("Subhead", color = Color.Gray, fontSize = 14.sp)
            }
            // Icons on the right would go here
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF232528)
@Composable
fun WorkoutsScreenPreview() {
    GetFitRPGTheme {
        WorkoutsScreen()
    }
}
