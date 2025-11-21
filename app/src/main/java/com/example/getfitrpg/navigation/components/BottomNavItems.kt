package com.example.getfitrpg.navigation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.ui.graphics.vector.ImageVector

// I'm using standard icons that look close to your image.
// You can replace these with your specific SVG/Vector assets later.
enum class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    Home("home", Icons.Default.Home, "Home"),
    Workout("workout", Icons.Default.ThumbUp, "Workout"), // Man lifting
    Profile("rpg", Icons.Default.Place, "RPG"),               // Sword
    Diet("diet", Icons.Default.DateRange, "Diet"),        // Salad bowl
    Timer("timer", Icons.Default.Star, "Timer")           // Timer
}