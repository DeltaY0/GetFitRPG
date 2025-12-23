package com.example.getfitrpg.core.designsystem
import androidx.compose.ui.graphics.Color

// THE CORE PALETTE
val RPGBackgroundDark = Color(0xFF0F172A)
val RPGSurfaceDark = Color(0xFF1E293B)
val RPGGreen = Color(0xFFA3E635)
val RPGYellow = Color(0xFFFBBF24)
val RPGErrorRed = Color(0xFFEF4444)
val RPGWhite = Color(0xFFF1F5F9)
val RPGGrey = Color(0xFF94A3B8)

val RPGPurple = Color(0xFF282239)
val RPGDarkBlue = Color(0xFF182331)
val RPGCoolPurple = Color(0xFF23223B)
val RPGRed = Color(0xFFEE4551)
val RPGCoolRed = Color(0xFFD47593)
val RPGBlue = Color(0xFF5487E7)
val RPGCoolBlue = Color(0xFF83A3D5)
val RPGCoolYellow = Color(0xFFCCCD7D)
val RPGCoolGreen = Color(0xFF8DD487)
val RPGBackgroundBlue = Color(0xFF020617)
val RPGContainerBlue = Color(0xFF0F172A)

val AppColorScheme = androidx.compose.material3.darkColorScheme(
    primary = RPGGreen,
    onPrimary = RPGBackgroundDark, // Text on Green buttons
    background = RPGBackgroundDark,
    onBackground = RPGWhite,
    surface = RPGSurfaceDark,
    onSurface = RPGWhite,
    error = RPGErrorRed,
    secondary = RPGYellow
)