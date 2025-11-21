package com.example.getfitrpg.core.designsystem
import androidx.compose.ui.graphics.Color

// THE CORE PALETTE
val BackgroundDark = Color(0xFF0F172A)
val SurfaceDark = Color(0xFF1E293B)
val PrimaryGreen = Color(0xFFA3E635)
val AccentYellow = Color(0xFFFBBF24)
val ErrorRed = Color(0xFFEF4444)
val TextWhite = Color(0xFFF1F5F9)
val TextGrey = Color(0xFF94A3B8)

val AppColorScheme = androidx.compose.material3.darkColorScheme(
    primary = PrimaryGreen,
    onPrimary = BackgroundDark, // Text on Green buttons
    background = BackgroundDark,
    onBackground = TextWhite,
    surface = SurfaceDark,
    onSurface = TextWhite,
    error = ErrorRed,
    secondary = AccentYellow
)