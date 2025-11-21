package com.example.getfitrpg.feature.profile

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.getfitrpg.core.designsystem.*
import com.example.getfitrpg.core.designsystem.components.SystemButton
import com.example.getfitrpg.core.designsystem.components.SystemCard
import com.example.getfitrpg.core.designsystem.components.SystemStatBar

@Composable
fun StatScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(16.dp)
            .verticalScroll(scrollState), // Allow scrolling on small screens
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // --- 1. Header: Name and Job ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "STATUS",
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = "NAME: JIN-WOO", // Replace with user name later
                    style = MaterialTheme.typography.labelMedium
                )
            }
            // Rank Badge
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(SurfaceDark)
                    .background(PrimaryGreen.copy(alpha = 0.1f)), // Subtle tint
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "E", // Rank
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 18.sp),
                    color = PrimaryGreen
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp),
            color = PrimaryGreen.copy(alpha = 0.3f)
        )

        // --- 2. Main Stats (Level & Job) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatLabelValue(label = "JOB", value = "NONE") // e.g. Assassin, Necromancer
            StatLabelValue(label = "TITLE", value = "WOLF SLAYER")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "LEVEL",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 24.sp),
                color = TextWhite
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = "12",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 48.sp),
                color = PrimaryGreen
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 3. Bars (HP/MP/XP) ---
        SystemCard {
            SystemStatBar(label = "HP", current = 1250, max = 1250, color = ErrorRed)
            Spacer(modifier = Modifier.height(12.dp))
            SystemStatBar(label = "MP", current = 850, max = 1000, color = AccentYellow)
            Spacer(modifier = Modifier.height(12.dp))
            // XP Bar (Using your PrimaryGreen)
            SystemStatBar(label = "EXP", current = 2400, max = 5000, color = PrimaryGreen)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 4. Attributes (STR, AGI, etc.) ---
        // The classic "Solo Leveling" stat grid
        SystemCard {
            Text(
                text = "ATTRIBUTES",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            AttributeRow(label = "STRENGTH", value = 24)
            AttributeRow(label = "VITALITY", value = 18)
            AttributeRow(label = "AGILITY", value = 32)
            AttributeRow(label = "INTELLIGENCE", value = 15)
            AttributeRow(label = "SENSE", value = 22)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "AVAILABLE POINTS: 3",
                style = MaterialTheme.typography.labelMedium.copy(color = PrimaryGreen),
                modifier = Modifier.align(Alignment.End)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 5. Actions ---
        SystemButton(text = "QUEST LOG", onClick = {})
    }
}

// --- Helper Composables for this screen only ---

@Composable
fun StatLabelValue(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(color = TextGrey)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun AttributeRow(label: String, value: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Preview
@Composable
fun StatScreenPreview() {
    GetFitRPGTheme {
        StatScreen()
    }
}