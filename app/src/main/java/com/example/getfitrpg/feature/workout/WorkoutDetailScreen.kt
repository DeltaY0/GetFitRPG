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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.getfitrpg.core.designsystem.GetFitRPGTheme
import com.example.getfitrpg.core.designsystem.MontserratFontFamily

// --- FIX: DEFINE COLORS LOCALLY ---
val RPGGreen = Color(0xFFA3E635)
val RPGWhite = Color(0xFFF1F5F9)
val RPGBackgroundBlue = Color(0xFF0F172A)
val IntYellow = Color(0xFFFACC15) // Moved here for visibility
// ----------------------------------

@Composable
fun WorkoutDetailScreen(onBackClick: () -> Unit = {}, onStartClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RPGBackgroundBlue)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header with back button
        WorkoutDetailHeader()

        // Preset Title Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Color(0xFFF5F7FB), RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF0B1220),
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onBackClick() }
                    )
                    Text(
                        text = "PRESET #6",
                        color = Color(0xFF0B1220),
                        fontFamily = MontserratFontFamily,
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp
                    )
                }
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color(0xFF0B1220),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Exercises List
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "WARMUP x3",
                    color = RPGWhite,
                    fontFamily = MontserratFontFamily,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
            }

            items(3) { index ->
                WorkoutDetailExerciseItem(
                    duration = when (index) {
                        0 -> "30r"
                        1 -> "15r"
                        else -> "0:30s"
                    },
                    name = when (index) {
                        0 -> "SITUPS"
                        1 -> "PULL UPS"
                        else -> "PULL UPS"
                    },
                    multiplier = "x1"
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    text = "ADD WORKOUT",
                    onClick = { },
                    backgroundColor = Color(0xFF60A5FA),
                    textColor = RPGWhite
                )
            }
        }

        // Bottom Buttons
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    text = "ADD SECTION",
                    onClick = { },
                    backgroundColor = Color(0xFFF5F7FB),
                    textColor = Color(0xFF7F6BFF),
                    modifier = Modifier.weight(1f)
                )
                Button(
                    text = "REORDER",
                    onClick = { },
                    backgroundColor = Color(0xFFF5F7FB),
                    textColor = Color(0xFF7F6BFF),
                    modifier = Modifier.weight(1f)
                )
            }

            Button(
                text = "SAVE PRESET",
                onClick = onStartClick,
                backgroundColor = RPGGreen,
                textColor = Color(0xFF0B1220)
            )
        }
    }
}

@Composable
private fun WorkoutDetailHeader() {
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
private fun WorkoutDetailExerciseItem(
    duration: String,
    name: String,
    multiplier: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A2D3E)),
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
                    color = RPGGreen,
                    fontFamily = MontserratFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp
                )
                Text(
                    text = name,
                    color = RPGWhite,
                    fontFamily = MontserratFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Text(
                text = multiplier,
                color = IntYellow,
                fontFamily = MontserratFontFamily,
                fontWeight = FontWeight.Black,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun Button(
    text: String,
    onClick: () -> Unit,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontFamily = MontserratFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WorkoutDetailScreenPreview() {
    GetFitRPGTheme {
        WorkoutDetailScreen()
    }
}
