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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
fun WorkoutScreen(onPresetSelected: (String) -> Unit = {}) {
    var searchQuery by remember { mutableStateOf("") }
    
    val presets = listOf(
        "Preset #1 - Legs",
        "Preset #2 - Chest",
        "Preset #3 - Back",
        "Preset #4 - Arms",
        "Preset #5 - Cardio",
        "Preset #6 - Full Body",
        "Preset #7 - Shoulders"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RPGBackgroundBlue)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        WorkoutHeader()

        Spacer(modifier = Modifier.height(8.dp))

        // Search & Filter
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search Presets", color = Color(0xFF6B7280)) },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = Color(0xFF6B7280)) },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFF1A2D3E),
                    focusedContainerColor = Color(0xFF1A2D3E),
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = RPGGreen
                ),
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = RPGWhite)
            )
            Icon(
                Icons.Default.Tune,
                contentDescription = "Filter",
                tint = RPGGreen,
                modifier = Modifier
                    .size(28.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { }
            )
        }

        // Presets List
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(presets.size) { index ->
                PresetCard(
                    title = presets[index],
                    tags = listOf("tags", "tags", "tags", "tags"),
                    onClick = { onPresetSelected(presets[index]) }
                )
            }
        }

        // New Preset Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(RPGGreen, RoundedCornerShape(16.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "NEW PRESET",
                color = Color(0xFF0B1220),
                fontFamily = MontserratFontFamily,
                fontWeight = FontWeight.Black,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
private fun WorkoutHeader() {
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
private fun PresetCard(
    title: String,
    tags: List<String>,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A2D3E))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = RPGWhite,
                    fontFamily = MontserratFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = tags.joinToString(" "),
                    color = Color(0xFF6B7280),
                    fontFamily = MontserratFontFamily,
                    fontSize = 12.sp
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("⋯", color = Color(0xFF6B7280), fontSize = 20.sp)
                Text("»", color = Color(0xFF6B7280), fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WorkoutScreenPreviews() {
    GetFitRPGTheme {
        WorkoutScreen()
    }
}
