package com.example.getfitrpg

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.*
import com.example.getfitrpg.ui.theme.*

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    // Default Colors to Override:
    /*
    background
    surface
    onPrimary
    onSecondary
    onTertiary
    onBackground
    onSurface
    onError
    */
)

@Composable
fun GetFitRPGTheme(
    dark_theme: Boolean = isSystemInDarkTheme(),
    dynamic_color: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamic_color && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current

            if(dark_theme)
                dynamicDarkColorScheme(context)
            else
                dynamicLightColorScheme(context)
        }

        dark_theme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if(!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            // TODO: TO BE IMPLEMENTED

        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )

}
