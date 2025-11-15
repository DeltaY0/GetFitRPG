package com.example.getfitrpg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.getfitrpg.pages.*
import com.example.getfitrpg.ui.theme.GetFitRPGTheme

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Splash : Screen("splash", "Splash", Icons.Default.Home) // Placeholder
    object Login : Screen("login", "Login", Icons.Default.Home) // Placeholder
    object Signup : Screen("signup", "Signup", Icons.Default.Home) // Placeholder
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Presets : Screen("presets", "Presets", Icons.Default.PlayArrow)
    object Stats : Screen("stats", "Stats", Icons.Default.Person)
    object Workouts : Screen("workouts", "Workouts", Icons.Default.Search)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
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
                                        icon = { Icon(screen.icon, contentDescription = screen.title) },
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
                        composable(Screen.Splash.route) { SplashScreen(navController) }
                        composable(Screen.Login.route) { Login(navController) }
                        composable(Screen.Signup.route) { Signup(navController) }
                        composable(Screen.Home.route) { Home() }
                        composable(Screen.Presets.route) { PresetsScreen() }
                        composable(Screen.Stats.route) { StatsScreen() }
                        composable(Screen.Workouts.route) { WorkoutsScreen() }
                        composable(Screen.Settings.route) { Text("Settings Screen") } 
                    }
                }
            }
        }
    }
}
