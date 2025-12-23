package com.example.getfitrpg.feature.splash

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.getfitrpg.core.designsystem.*

import com.example.getfitrpg.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onInitializationComplete: () -> Unit
) {
    // State to control the progress bar (Starts at 0.0, goes to 1.0)
    var progressTarget by remember { mutableFloatStateOf(0f) }

    // Animate the progress smoothly
    val progress by animateFloatAsState(
        targetValue = progressTarget,
        animationSpec = tween(durationMillis = 2000, easing = LinearEasing),
        label = "BootProgress"
    )

    LaunchedEffect(key1 = true) {
        // Trigger the animation to fill the bar
        progressTarget = 1f

        // Wait for animation to finish + a tiny buffer
        delay(2100)
        onInitializationComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RPGBackgroundDark),
        contentAlignment = Alignment.Center
    ) {
        // Top: Logo
        Image(
            painter = painterResource(id = R.drawable.logo_with_text_dark),
            contentDescription = "Logo",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 100.dp)
                .width(200.dp)
        )

        // Center: Loading System
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 48.dp)
        ) {
            // 1. Header
            Text(
                text = "SYSTEM",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = RPGYellow,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 4.sp
                )
            )

            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "LOADING...",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = RPGGrey
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 2. The Loading Bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(18.dp),
                color = RPGGreen,
                trackColor = RPGSurfaceDark, // The empty part of the bar
                strokeCap = StrokeCap.Round, // "Butt" means sharp edges (Digital style), "Round" is too soft
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Optional: Percentage Text (e.g., "45%")
            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = RPGGreen
                )
            )
        }

        // Version Footer
        Text(
            text = "V 0.3.11",
            style = MaterialTheme.typography.headlineMedium.copy(color = RPGErrorRed),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}

@Preview
@Composable
fun SplashPreview() {
    GetFitRPGTheme {
        SplashScreen(onInitializationComplete = {})
    }
}