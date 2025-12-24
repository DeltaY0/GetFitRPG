package com.example.getfitrpg.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.getfitrpg.PlaceholderScreen
import com.example.getfitrpg.feature.auth.AuthManager
import com.example.getfitrpg.feature.auth.ForgetPassword.ForgetPassword1Screen
import com.example.getfitrpg.feature.auth.ForgetPassword.ForgetPassword2Screen
import com.example.getfitrpg.feature.auth.ForgetPassword.ForgetPassword3Screen
import com.example.getfitrpg.feature.auth.ForgetPassword.OtpScreen
import com.example.getfitrpg.feature.auth.login.LoginScreen
import com.example.getfitrpg.feature.auth.signup.HeightScreen
import com.example.getfitrpg.feature.auth.signup.OnboardingScreen
import com.example.getfitrpg.feature.auth.signup.SignupScreen
import com.example.getfitrpg.feature.auth.signup.WeightScreen
import com.example.getfitrpg.feature.splash.SplashScreen
import com.example.getfitrpg.feature.timer.TimerScreen
import com.example.getfitrpg.feature.workout.WorkoutScreen
import com.example.getfitrpg.feature.diet_ai.DietAIScreen
import com.example.getfitrpg.feature.food.FoodDetectionScreen
import com.example.getfitrpg.feature.profile.StatScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Splash.route,
    oobCode: String? // Add this parameter to receive the code from MainActivity
) {
    val authManager = remember { AuthManager() }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // 1. Splash
        composable(Screen.Splash.route) {
            SplashScreen(
                onInitializationComplete = {
                    // If there's a password reset code (from deep link), navigate to the reset screen
                    if (oobCode != null) {
                        navController.navigate("${Screen.ForgetPassword2.route}/$oobCode") {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    } else {
                        // Otherwise, go to the login screen
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        // 2. Auth Flow
        composable(Screen.Login.route) {
            LoginScreen(
                authManager = authManager,
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
                authManager = authManager,
                onRegisterClicked = {
                    // After successful signup, go to Onboarding
                    navController.navigate(Screen.Onboarding.route)
                },
                onLoginClicked = { navController.popBackStack() }
            )
        }

        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onStartClicked = {
                    navController.navigate(Screen.Weight.route)
                }
            )
        }

        composable(Screen.Weight.route) {
            WeightScreen(
                onNextClicked = {
                    navController.navigate(Screen.Height.route)
                },
                onBackClicked = {
                    navController.popBackStack()
                },
                authManager = authManager
            )
        }

        composable(Screen.Height.route) {
            HeightScreen(
                onNextClicked = {
                    navController.navigate(Screen.Home.route) {
                        // Clear the back stack up to Login so user can't go back to signup flow
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onBackClicked = {
                    navController.popBackStack()
                },
                authManager = authManager
            )
        }

        composable(Screen.ForgetPassword1.route) {
            ForgetPassword1Screen(
                authManager = authManager,
                onBackClicked = { navController.popBackStack() },
                onLoginClicked = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToOtp = {
                    navController.navigate(Screen.Otp.route)
                }
            )
        }

        composable(Screen.Otp.route) {
            OtpScreen(
                authManager = authManager,
                onBackClicked = { navController.popBackStack() },
                onVerifySuccess = { code ->
                    // Navigate to the actual Reset Password screen with the verification code
                    navController.navigate("${Screen.ForgetPassword2.route}/$code") {
                        // Clear OTP from back stack so user cannot return to it
                        popUpTo(Screen.Otp.route) { inclusive = true }
                    }
                }
            )
        }

        composable("${Screen.ForgetPassword2.route}/{oobCode}") { backStackEntry ->
            val code = backStackEntry.arguments?.getString("oobCode")
            if (code != null) {
                ForgetPassword2Screen(
                    authManager = authManager,
                    code = code,
                    onBackClicked = { navController.popBackStack() },
                    onResetPasswordClicked = {
                        navController.navigate(Screen.ForgetPassword3.route) {
                            popUpTo(Screen.ForgetPassword2.route) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(Screen.ForgetPassword3.route) {
            ForgetPassword3Screen(
                onBackToLoginClicked = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // 3. Main App Flow
        composable(Screen.Home.route) {
            StatScreen()
        }

        composable(Screen.Workout.route) {
            WorkoutScreen()
        }

        composable(Screen.Stats.route) {
            StatScreen()
        }

        composable(Screen.DietAI.route) {
            DietAIScreen()
        }

        composable(Screen.Timer.route) {
            TimerScreen()
        }

        composable(Screen.Food.route) {
            FoodDetectionScreen()
        }
    }
}

// Define your simple string routes here
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Onboarding : Screen("onboarding")
    object Weight : Screen("weight")
    object Height : Screen("height")
    object ForgetPassword1 : Screen("forget_password_1")
    object Otp : Screen("otp")
    object ForgetPassword2 : Screen("forget_password_2")
    object ForgetPassword3 : Screen("forget_password_3")
    object Home : Screen("home")
    object Workout : Screen("workout")
    object Stats : Screen("profile")
    object DietAI : Screen("diet_ai")
    object Timer : Screen("timer")
    object Food : Screen("food")
}