package com.example.getfitrpg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

sealed class Screen(val route: String, val title: String) {
    object Splash : Screen("splash", "Splash")
    object Login : Screen("login", "Login")
    object Signup : Screen("signup", "Signup")
    object Home : Screen("home", "Home")
    object Presets : Screen("presets", "Presets")
    object Stats : Screen("stats", "Stats")
    object Workouts : Screen("workouts", "Workouts")
    object Settings : Screen("settings", "Settings")
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Presets,
    Screen.Stats,
    Screen.Workouts,
    Screen.Settings
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

        }
    }
}