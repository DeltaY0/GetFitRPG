package com.example.getfitrpg.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.getfitrpg.PlaceholderScreen

import com.example.getfitrpg.feature.auth.login.LoginScreen
import com.example.getfitrpg.feature.auth.signup.SignupScreen
import com.example.getfitrpg.feature.splash.SplashScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // 1. Splash
        composable(Screen.Splash.route) {
            SplashScreen(
                onInitializationComplete = {
                    // Pop Splash so user can't go back to it
                    // TODO: fix navigation to login/onboarding
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // 2. Auth Flow
        composable(Screen.Login.route) {
            LoginScreen(
                /*onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToSignup = {
                    navController.navigate(Screen.Signup.route)
                }*/
            )
        }

        composable(Screen.Signup.route) {
            SignupScreen(
                /*onSignupSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Signup.route) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }*/
            )
        }

        // 3. Main App Flow
        composable(Screen.Home.route) {
            // HomeScreen()
            PlaceholderScreen("DASHBOARD")
        }

        composable(Screen.Workout.route) {
            // WorkoutScreen()
            PlaceholderScreen("WORKOUT HUB")
        }

        composable(Screen.Stats.route) {
            // StatScreen()
            PlaceholderScreen("RPG STATS")
        }

        composable(Screen.DietAI.route) {
            // DietAIScreen()
            PlaceholderScreen("DIET AI")
        }

        composable(Screen.Timer.route) {
            // TimerScreen()
            PlaceholderScreen("TIMER")
        }
    }
}

// Define your simple string routes here
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Home : Screen("home")
    object Workout : Screen("workout")
    object Stats : Screen("profile")
    object DietAI : Screen("diet_ai")
    object Timer : Screen("timer")
}