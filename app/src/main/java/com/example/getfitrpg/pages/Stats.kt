package com.example.getfitrpg.pages

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.getfitrpg.ui.theme.GetFitRPGTheme

// Colors from the design
private val DarkGreen = Color(0xFF3A4B3F)
private val LightGreen = Color(0xFF8BC34A)
private val BackgroundDark = Color(0xFF232528)
private val AlmostWhite = Color(0xFFE0E0E0)
private val ProgressTrackGreen = Color(0xFF334339)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Stats", color = AlmostWhite) },
                navigationIcon = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = AlmostWhite)
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = AlmostWhite)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundDark)
            )
        },
        containerColor = BackgroundDark
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // User Info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(DarkGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User Avatar",
                        modifier = Modifier.size(48.dp),
                        tint = AlmostWhite
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Zeyad", color = AlmostWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Level:99", color = Color.Gray, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // XP Bar
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("XP", color = AlmostWhite, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = 0.6f,
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                    color = LightGreen,
                    trackColor = DarkGreen
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Height and Weight
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                StatCard(value = "21", label = "height")
                StatCard(value = "21", label = "weight")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Circular Stats
            val circularStatItems = listOf(
                "ICON" to 0.3f, "ICON" to 0.75f,
                "ICON" to 0.9f, "ICON" to 0.11f
            )

            Column {
                Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
                    CircularStat(label = circularStatItems[0].first, progress = circularStatItems[0].second)
                    CircularStat(label = circularStatItems[1].first, progress = circularStatItems[1].second)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
                    CircularStat(label = circularStatItems[2].first, progress = circularStatItems[2].second)
                    CircularStat(label = circularStatItems[3].first, progress = circularStatItems[3].second)
                }
            }
        }
    }
}

@Composable
fun StatCard(value: String, label: String) {
    Card(
        modifier = Modifier.size(width = 150.dp, height = 100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkGreen)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontSize = 32.sp, color = AlmostWhite, fontWeight = FontWeight.Bold)
            Text(label, fontSize = 16.sp, color = AlmostWhite)
        }
    }
}

@Composable
fun CircularStat(label: String, progress: Float) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(120.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 20f
            // Background arc
            drawArc(
                color = ProgressTrackGreen,
                startAngle = -225f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            // Progress arc
            drawArc(
                color = LightGreen,
                startAngle = -225f,
                sweepAngle = 270f * progress,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, color = AlmostWhite, fontSize = 14.sp)
            Text("${(progress * 100).toInt()}%", color = AlmostWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF232528)
@Composable
fun StatsScreenPreview() {
    GetFitRPGTheme {
        StatsScreen()
    }
}
