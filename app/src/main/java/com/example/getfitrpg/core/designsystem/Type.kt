package com.example.getfitrpg.core.designsystem

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.getfitrpg.R


val JetbrainsFontFamily = FontFamily(
    Font(R.font.jetbrainsmono_regular, FontWeight.Normal),
    Font(R.font.jetbrainsmono_bold, FontWeight.Bold),
    Font(R.font.jetbrainsmono_medium, FontWeight.Medium),
    Font(R.font.jetbrainsmono_light, FontWeight.Light),
    Font(R.font.jetbrainsmono_semibold, FontWeight.SemiBold),
    Font(R.font.jetbrainsmono_extrabold, FontWeight.Black)
)

val MontserratFontFamily = FontFamily(
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_bold, FontWeight.Bold),
    Font(R.font.montserrat_medium, FontWeight.Medium),
    Font(R.font.montserrat_light, FontWeight.Light),
    Font(R.font.montserrat_semibold, FontWeight.SemiBold),
    Font(R.font.montserrat_black, FontWeight.Black)
)

val OnderFontFamily = FontFamily(
    Font(R.font.onder_regular, FontWeight.Normal)
)

val AppTypography = Typography(

    // Large Headers: "SYSTEM INITIALIZED" / "LEVEL UP"
    headlineLarge = TextStyle(
        fontFamily = OnderFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        color = RPGGreen
    ),

    headlineMedium = TextStyle(
        fontFamily = OnderFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        color = RPGGreen
    ),

    headlineSmall = TextStyle(
        fontFamily = OnderFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        color = RPGGreen
    ),

    // Section Headers: "Current Quest" / "Dashboard"
    titleMedium = TextStyle(
        fontFamily = MontserratFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = RPGWhite
    ),

    // Stats / Data: "XP: 500" / "STR: 10" -> Monospace is key for the System look
    labelMedium = TextStyle(
        fontFamily = MontserratFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        color = RPGGrey // Using your simplified Grey
    ),

    // Standard Body Text
    bodyLarge = TextStyle(
        fontFamily = MontserratFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = RPGWhite
    )
)