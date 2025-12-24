package com.example.getfitrpg.feature.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.getfitrpg.core.designsystem.GetFitRPGTheme
import com.example.getfitrpg.core.designsystem.MontserratFontFamily
import com.example.getfitrpg.core.designsystem.RPGBackgroundBlue
import com.example.getfitrpg.core.designsystem.RPGGreen
import com.example.getfitrpg.core.designsystem.RPGWhite

@Composable
fun ActiveWorkoutScreen() {
    var isRunning by remember { mutableStateOf(true) }
    var currentSet by remember { mutableStateOf(1) }
    var totalSets by remember { mutableStateOf(3) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RPGBackgroundBlue)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        ActiveWorkoutHeader()

        // Video Preview
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Color(0xFF1A2D3E), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "03:31",
                    color = RPGWhite,
                    fontFamily = MontserratFontFamily,
                    fontWeight = FontWeight.Black,
                    fontSize = 32.sp
                )
                Text(
                    text = "TRAINING\nVIDEO PREVIEW",
                    color = Color(0xFF6B7280),
                    fontFamily = MontserratFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }

        // Exercises
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "POWERSET #1",
                    color = RPGWhite,
                    fontFamily = MontserratFontFamily,
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp
                )
                Text(
                    text = "$currentSet/$totalSets",
                    color = Color(0xFF60A5FA),
                    fontFamily = MontserratFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            items(3) { index ->
                ActiveExerciseItem(
                    duration = when (index) {
                        0 -> "0:30s"
                        1 -> "30r"
                        else -> "15r"
                    },
                    name = when (index) {
                        0 -> "HAMMER CURLS"
                        1 -> "SITUPS"
                        else -> "PULL UPS"
                    },
                    isActive = index == 0
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "POWERSET #2",
                    color = RPGWhite,
                    fontFamily = MontserratFontFamily,
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp
                )
            }

            item {
                ActiveExerciseItem(
                    duration = "0:30s",
                    name = "PULL UPS",
                    isActive = false
                )
            }
        }

        // Pause Button
        PausePlayButton(isRunning = isRunning, onToggle = { isRunning = !isRunning })
    }
}

@Composable
private fun ActiveWorkoutHeader() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color(0xFFFBBF24), RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "R", color = Color(0xFF0B1220), fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
            Column {
                Text(text = "PLAYER NAME", color = Color(0xFF6B7280), fontSize = 12.sp, fontFamily = MontserratFontFamily)
                Text(text = "CLASS NAME", color = RPGWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = MontserratFontFamily)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "LVL 027", color = RPGWhite, fontWeight = FontWeight.Bold, fontSize = 20.sp, fontFamily = MontserratFontFamily)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(Color(0xFF1A2D3E), RoundedCornerShape(2.dp))) {
            Box(modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(4.dp)
                .background(RPGGreen, RoundedCornerShape(2.dp)))
        }
    }
}

@Composable
private fun ActiveExerciseItem(
    duration: String,
    name: String,
    isActive: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) Color(0xFF5487E7) else Color(0xFF1A2D3E)
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = duration,
                    color = if (isActive) Color(0xFF4AE8C0) else Color(0xFF6B7280),
                    fontFamily = MontserratFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp
                )
                Text(
                    text = name,
                    color = if (isActive) Color(0xFFFBD34E) else RPGWhite,
                    fontFamily = MontserratFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PausePlayButton(isRunning: Boolean, onToggle: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F7FB)),
        shape = RoundedCornerShape(14.dp),
        onClick = onToggle
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isRunning) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                contentDescription = if (isRunning) "Pause" else "Play",
                tint = Color(0xFF0B1220),
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ActiveWorkoutScreenPreview() {
    GetFitRPGTheme {
        ActiveWorkoutScreen()
    }
}
