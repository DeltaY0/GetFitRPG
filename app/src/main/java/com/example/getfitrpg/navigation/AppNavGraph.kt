package com.example.getfitrpg.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.getfitrpg.PlaceholderScreen
import com.example.getfitrpg.feature.auth.ForgetPassword.ForgetPassword1Screen
import com.example.getfitrpg.feature.auth.ForgetPassword.ForgetPassword2Screen
import com.example.getfitrpg.feature.auth.ForgetPassword.ForgetPassword3Screen
import com.example.getfitrpg.feature.auth.ForgetPassword.OtpScreen
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
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // 2. Auth Flow
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToSignup = {
                    navController.navigate(Screen.Signup.route)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(Screen.ForgetPassword1.route)
                }
            )
        }

        composable(Screen.Signup.route) {
            SignupScreen(
                onRegisterClicked = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onLoginClicked = { navController.popBackStack() }
            )
        }

        composable(Screen.ForgetPassword1.route) {
            ForgetPassword1Screen(
                onBackClicked = { navController.popBackStack() },
                onLoginClicked = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onSendCodeClicked = {
                    navController.navigate(Screen.Otp.route)
                }
            )
        }

        composable(Screen.Otp.route) {
            OtpScreen(
                onBackClicked = { navController.popBackStack() },
                onVerifySuccess = {
                    navController.navigate(Screen.ForgetPassword2.route)
                }
            )
        }

        composable(Screen.ForgetPassword2.route) {
            ForgetPassword2Screen(
                onBackClicked = { navController.popBackStack() },
                onResetPasswordClicked = { 
                    navController.navigate(Screen.ForgetPassword3.route) {
                        popUpTo(Screen.ForgetPassword2.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.ForgetPassword3.route) {
            ForgetPassword3Screen(
                onBackClicked = { navController.popBackStack() },
                onBackToLoginClicked = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
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
    object ForgetPassword1 : Screen("forget_password_1")
    object Otp : Screen("otp")
    object ForgetPassword2 : Screen("forget_password_2")
    object ForgetPassword3 : Screen("forget_password_3")
    object Home : Screen("home")
    object Workout : Screen("workout")
    object Stats : Screen("profile")
    object DietAI : Screen("diet_ai")
    object Timer : Screen("timer")
}
