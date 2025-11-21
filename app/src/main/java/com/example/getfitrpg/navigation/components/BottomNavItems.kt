package com.example.getfitrpg.navigation.components

import com.example.getfitrpg.R

enum class BottomNavItem(
    val route: String,
    val icon: Int,
    val label: String
) {
    Home("home", R.drawable.icon_home, "Home"),
    Workout("workout", R.drawable.icon_workout, "Workout"),
    Profile("profile", R.drawable.icon_sword, "Profile"),
    Diet("diet_ai", R.drawable.icon_food, "Diet"),
    Timer("timer", R.drawable.icon_dumbbell_timer, "Timer")
}