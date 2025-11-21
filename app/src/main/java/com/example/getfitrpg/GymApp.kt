package com.example.getfitrpg

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import com.example.getfitrpg.core.designsystem.BackgroundDark
import com.example.getfitrpg.core.designsystem.GetFitRPGTheme
import com.example.getfitrpg.core.designsystem.PrimaryGreen
import com.example.getfitrpg.navigation.AppNavGraph
import com.example.getfitrpg.navigation.Screen
import com.example.getfitrpg.navigation.components.AnimatedBottomBar
import com.example.getfitrpg.navigation.components.BottomNavItem

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    // Logic to determine if we should show the bottom bar
    // (e.g., Hide on Splash and Login screens)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in listOf(Screen.Home.route, "workout", "rpg")

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                // map currentRoute to your BottomNavItem enum to update the selected tab
                val currentTab = BottomNavItem.entries.find { it.route == currentRoute } ?: BottomNavItem.Home

                AnimatedBottomBar(
                    selectedItem = currentTab,
                    onItemSelected = { item ->
                        // Standard Bottom Nav navigation logic
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        },
        containerColor = BackgroundDark
    ) { innerPadding ->
        AppNavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = androidx.compose.material3.MaterialTheme.typography.headlineLarge,
            color = PrimaryGreen
        )
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    GetFitRPGTheme {
        MainScreen()
    }
}
