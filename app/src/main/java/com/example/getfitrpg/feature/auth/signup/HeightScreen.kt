package com.example.getfitrpg.feature.auth.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.getfitrpg.core.designsystem.GetFitRPGTheme
import com.example.getfitrpg.core.designsystem.RPGYellow
import com.example.getfitrpg.feature.auth.AuthManager

@Composable
fun HeightScreen(
    onNextClicked: () -> Unit,
    onBackClicked: () -> Unit,
    authManager: AuthManager
) {
    var height by remember { mutableFloatStateOf(170f) }
    var unit by remember { mutableStateOf("CM") }

    val minHeight = if (unit == "CM") 100f else 40f
    val maxHeight = if (unit == "CM") 250f else 100f

    val onHeightChange = { newHeight: Float ->
        height = newHeight.coerceIn(minHeight, maxHeight)
    }

    MeasurementScreenTemplate(
        title = "WHAT IS YOUR HEIGHT?",
        value = height,
        unit = unit,
        units = listOf("CM", "IN"),
        onValueChange = onHeightChange,
        onUnitChange = { newUnit ->
            if (unit != newUnit) {
                if (newUnit == "IN") {
                    height = (height / 2.54f).coerceIn(40f, 100f)
                } else {
                    height = (height * 2.54f).coerceIn(100f, 250f)
                }
                unit = newUnit
            }
        },
        onNextClicked = {
            // Save height to Firebase
            // Convert to CM if needed for standard storage
            val heightInCm = if (unit == "IN") height * 2.54f else height
            authManager.updateUserHeight(heightInCm)
            onNextClicked()
        },
        onBackClicked = onBackClicked,
        themeColor = RPGYellow
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF101828)
@Composable
fun HeightScreenPreview() {
    GetFitRPGTheme {
        HeightScreen(onNextClicked = {}, onBackClicked = {}, authManager = AuthManager())
    }
}
