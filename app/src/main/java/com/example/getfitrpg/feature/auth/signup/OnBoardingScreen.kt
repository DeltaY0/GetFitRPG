package com.example.getfitrpg.feature.auth.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.getfitrpg.core.designsystem.GetFitRPGTheme
import com.example.getfitrpg.core.designsystem.RPGBackgroundBlue
import com.example.getfitrpg.core.designsystem.RPGCoolBlue
import com.example.getfitrpg.core.designsystem.RPGCoolGreen
import com.example.getfitrpg.core.designsystem.RPGCoolRed
import com.example.getfitrpg.core.designsystem.RPGCoolYellow
import com.example.getfitrpg.core.designsystem.RPGGrey
import com.example.getfitrpg.core.designsystem.RPGWhite
import com.example.getfitrpg.core.designsystem.components.FeatureCard
import com.example.getfitrpg.core.designsystem.components.RPGButton

@Composable
fun OnboardingScreen(onStartClicked: () -> Unit) {
    var selectedFeature by remember { mutableStateOf<String?>(null) }

    // TODO: load the correct icons instead of the placeholders
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RPGBackgroundBlue)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(28.dp))
        Text(
            text = "GET FIT RPG",
            style = MaterialTheme.typography.headlineLarge,
            color = RPGWhite,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "FEATURES",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Gray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)
        ) {
            FeatureCard(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                title = "ARCHIVE",
                titleColor = RPGCoolBlue,
                description = "2,000+ Predefined Workouts, each with it's own tutorial video built in.\n\nFully Offline. No internet required.",
                icon = Icons.Default.Archive,
                isSelected = selectedFeature == "ARCHIVE",
                onClick = { selectedFeature = "ARCHIVE" }
            )
            FeatureCard(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                title = "FOOD SCAN",
                titleColor = RPGCoolYellow,
                description = "Scan food, get estimated calories per gram.\n\nStay healthy. Meet your goals. Level up.",
                icon = Icons.Default.CameraAlt,
                isSelected = selectedFeature == "FOOD SCAN",
                onClick = { selectedFeature = "FOOD SCAN" }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        FeatureCard(
            modifier = Modifier.fillMaxWidth(),
            title = "THE SYSTEM",
            titleColor = RPGGrey,
            description = "Your life, gamified. Earn XP for every rep, level up your stats (STR, AGI, INT), and unlock your potential.",
            icon = Icons.Default.Computer,
            isSelected = selectedFeature == "THE SYSTEM",
            onClick = { selectedFeature = "THE SYSTEM" }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)
        ) {
            FeatureCard(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                title = "FOCUS MODE",
                titleColor = RPGCoolRed,
                description = "Pomodoro timers for HIIT, stretching, or focused grinding.",
                icon = Icons.Default.Timer,
                isSelected = selectedFeature == "FOCUS MODE",
                onClick = { selectedFeature = "FOCUS MODE" }
            )
            FeatureCard(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                title = "AI SENSEI",
                titleColor = RPGCoolGreen,
                description = "Custom workout plans generated instantly based on your goals.\n\nCustom diet plans curated for your dietary restrictions.\n",
                icon = Icons.Default.SmartToy,
                isSelected = selectedFeature == "AI SENSEI",
                onClick = { selectedFeature = "AI SENSEI" }
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(24.dp))

        RPGButton(
            text = "START",
            onClick = onStartClicked,
            containerColor = Color.White,
            contentColor = Color.Black,
            modifier = Modifier.padding(bottom = 24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    GetFitRPGTheme {
        OnboardingScreen(onStartClicked = {})
    }
}
