package com.example.getfitrpg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.getfitrpg.pages.Home
import com.example.getfitrpg.pages.Login
import com.example.getfitrpg.pages.Signup
import com.example.getfitrpg.pages.SplashScreen
import com.example.getfitrpg.ui.theme.GetFitRPGTheme

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
            GetFitRPGTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        val showBottomBar = when (currentDestination?.route) {
                            Screen.Login.route, Screen.Signup.route, Screen.Splash.route -> false
                            else -> true
                        }
                        if (showBottomBar) {
                            NavigationBar {
                                bottomNavItems.forEach { screen ->
                                    NavigationBarItem(
                                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                                        label = { Text(screen.title) },
                                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                        onClick = {
                                            navController.navigate(screen.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Splash.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Splash.route) { SplashScreen(navController = navController, modifier = Modifier) }
                        composable(Screen.Login.route) { Login(navController = navController, modifier = Modifier) }
                        composable(Screen.Signup.route) { Signup(navController = navController, modifier = Modifier) }
                        composable(Screen.Home.route) { Home(modifier = Modifier) }
                        // Add composables for Presets, Stats, Workouts, and Settings here
                    }
                }
            }
        }
    }
}
